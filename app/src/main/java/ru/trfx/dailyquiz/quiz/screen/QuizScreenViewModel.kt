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
import ru.trfx.dailyquiz.quiz.Quiz
import ru.trfx.dailyquiz.quiz.repository.QuizRepository
import ru.trfx.dailyquiz.ui.component.AnswerOptionState
import ru.trfx.dailyquiz.ui.component.RadioButtonState
import timber.log.Timber

sealed interface QuizScreenUiState {
    data object Loading : QuizScreenUiState
    data object Error : QuizScreenUiState
    data class Loaded(val quiz: Quiz) : QuizScreenUiState
}

class QuizScreenViewModel(
    private val quizRepository: QuizRepository,
) : ViewModel() {
    companion object {
        const val QUIZ_DURATION = 15f
        private const val TIMER_INTERVAL = 50L
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

    private val _answerOptions = MutableStateFlow<List<AnswerOptionState>>(emptyList())
    val answerOptions = _answerOptions.asStateFlow()

    private val _timeoutEventFlow = MutableSharedFlow<Unit>()
    val timeoutEventFlow = _timeoutEventFlow.asSharedFlow()

    private var selectedOptionIndex = -1
    private val answers = mutableListOf<Int>()

    fun loadQuiz(category: Category?, difficulty: Difficulty?) {
        viewModelScope.launch {
            Timber.i("Loading quiz ($category, $difficulty)")
            try {
                // Prepare quiz
                val quiz = quizRepository.getQuiz(5, category, difficulty)
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
                _state.value = QuizScreenUiState.Loaded(quiz)
                selectedOptionIndex = -1
                answers.clear()
            } catch (e: Exception) {
                _state.value = QuizScreenUiState.Error
            }
        }
    }

    fun advanceQuiz() {
        Timber.i("Advancing quiz")
        check(selectedOptionIndex >= 0)
        val state = _state.value as QuizScreenUiState.Loaded

        answers += selectedOptionIndex
        selectedOptionIndex = -1

        if (questionNumber.value == state.quiz.questions.size) {
            Timber.i("Reached last question, showing quiz results")
            TODO()
        } else {
            _buttonEnabled.value = false
            _questionNumber.value++
            _answerOptions.value = state.quiz.questions[_questionNumber.value - 1].options.map {
                AnswerOptionState(it, RadioButtonState.Off)
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
