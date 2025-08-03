package ru.trfx.dailyquiz.quiz.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.trfx.dailyquiz.quiz.Category
import ru.trfx.dailyquiz.quiz.Difficulty
import ru.trfx.dailyquiz.quiz.QuizHandler
import ru.trfx.dailyquiz.quiz.repository.QuizRepository
import ru.trfx.dailyquiz.ui.component.AnswerOptionState
import ru.trfx.dailyquiz.ui.component.RadioButtonState
import timber.log.Timber

sealed interface QuizScreenUiState {
    data object Loading : QuizScreenUiState
    data object Error : QuizScreenUiState
    data object Loaded : QuizScreenUiState
    data object Results : QuizScreenUiState
}

class QuizScreenViewModel(
    private val quizRepository: QuizRepository,
) : ViewModel() {
    companion object {
        const val QUIZ_DURATION = 15f
        private const val TIMER_INTERVAL = 50L
        private const val REVEAL_DURATION = 900L
    }

    private val _state = MutableStateFlow<QuizScreenUiState>(QuizScreenUiState.Loading)
    val state = _state.asStateFlow()

    private var startTime: Long = 0L
    private var timerJob: Job? = null

    private val _elapsedSeconds = MutableStateFlow(0f)
    val elapsedSeconds = _elapsedSeconds.asStateFlow()
    val totalSeconds get() = QUIZ_DURATION

    private val _questionNumber = MutableStateFlow(1)
    val questionNumber = _questionNumber.asStateFlow()

    private val _buttonEnabled = MutableStateFlow(false)
    val buttonEnabled = _buttonEnabled.asStateFlow()

    private val _optionsEnabled = MutableStateFlow(true)
    val optionsEnabled = _optionsEnabled.asStateFlow()

    private val _answerOptions = MutableStateFlow<List<AnswerOptionState>>(emptyList())
    val answerOptions = _answerOptions.asStateFlow()

    private val _timeoutEventFlow = MutableSharedFlow<Unit>()
    val timeoutEventFlow = _timeoutEventFlow.asSharedFlow()

    private var selectedOptionIndex = -1

    fun loadQuiz(category: Category?, difficulty: Difficulty?) {
        viewModelScope.launch {
            Timber.i("Loading quiz ($category, $difficulty)")
            try {
                // Prepare quiz
                QuizHandler.currentQuiz = null
                val quiz = quizRepository.getQuiz(5, category, difficulty)
                QuizHandler.currentQuiz = quiz
                _elapsedSeconds.value = 0f
                _questionNumber.value = 1
                _answerOptions.value = quiz.questions[0].options.map {
                    AnswerOptionState(it, RadioButtonState.Off)
                }

                // Start timer
                startTime = System.currentTimeMillis()
                timerJob?.cancel()
                timerJob = launch {
                    Timber.i("Starting quiz timer")
                    while (_elapsedSeconds.value <= QUIZ_DURATION) {
                        _elapsedSeconds.value = (System.currentTimeMillis() - startTime) / 1000f
                        delay(TIMER_INTERVAL)
                    }

                    Timber.i("Quiz timeout")
                    _timeoutEventFlow.tryEmit(Unit)
                }

                // Update state
                _state.value = QuizScreenUiState.Loaded
                selectedOptionIndex = -1
                _buttonEnabled.value = true
                _optionsEnabled.value = true
            } catch (e: Exception) {
                _state.value = QuizScreenUiState.Error
            }
        }
    }

    fun advanceQuiz() {
        check(selectedOptionIndex >= 0)
        QuizHandler.recordAnswer(selectedOptionIndex)

        // Reveal answer
        Timber.i("Revealing answer")
        val currentQuestion = QuizHandler.getQuestion(_questionNumber.value)
        val newOptions = mutableListOf<AnswerOptionState>()
        for (i in _answerOptions.value.indices) {
            val option = _answerOptions.value[i]

            @Suppress("CascadeIf")
            newOptions += option.copy(
                buttonState = if (i == currentQuestion.correctOptionIndex) {
                    RadioButtonState.Correct
                } else if (i == selectedOptionIndex) {
                    RadioButtonState.Incorrect
                } else RadioButtonState.Off
            )
        }
        _answerOptions.value = newOptions
        selectedOptionIndex = -1
        _optionsEnabled.value = false
        _buttonEnabled.value = false

        // Advance the quiz
        viewModelScope.launch {
            delay(REVEAL_DURATION)
            _optionsEnabled.value = true
            _buttonEnabled.value = true
            Timber.i("Advancing quiz")

            if (questionNumber.value == QuizHandler.questionCount) {
                Timber.i("Reached last question, showing quiz results")
                _state.value = QuizScreenUiState.Results
            } else {
                _buttonEnabled.value = false
                _questionNumber.value++
                _answerOptions.value = QuizHandler.getQuestion(_questionNumber.value).options.map {
                    AnswerOptionState(it, RadioButtonState.Off)
                }
            }
        }
    }

    fun onOptionSelected(index: Int) {
        selectedOptionIndex = index
        _buttonEnabled.value = true
        _answerOptions.value = _answerOptions.value.mapIndexed { i, option ->
            option.copy(
                buttonState = if (i == index) {
                    RadioButtonState.Selected
                } else RadioButtonState.Off
            )
        }
    }
}
