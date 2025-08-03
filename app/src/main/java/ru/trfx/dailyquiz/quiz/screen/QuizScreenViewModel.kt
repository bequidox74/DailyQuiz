package ru.trfx.dailyquiz.quiz.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.trfx.dailyquiz.quiz.Category
import ru.trfx.dailyquiz.quiz.CompletedQuiz
import ru.trfx.dailyquiz.quiz.Difficulty
import ru.trfx.dailyquiz.quiz.Quiz
import ru.trfx.dailyquiz.quiz.repository.QuizRepository
import ru.trfx.dailyquiz.ui.component.AnswerOptionState
import ru.trfx.dailyquiz.ui.component.RadioButtonState
import timber.log.Timber

sealed interface QuizScreenUiState {
    data object Loading : QuizScreenUiState
    data object Error : QuizScreenUiState
    data object Loaded : QuizScreenUiState
    data object Timeout : QuizScreenUiState
    data object Results : QuizScreenUiState
}

class QuizScreenViewModel(
    private val quizRepository: QuizRepository,
) : ViewModel() {
    companion object {
        const val QUIZ_DURATION = 20f

        private const val TIMER_INTERVAL = 50L
        private const val REVEAL_DURATION = 900L
        private const val QUESTION_COUNT = 5
    }

    private val _state = MutableStateFlow<QuizScreenUiState>(QuizScreenUiState.Loading)
    val state = _state.asStateFlow()

    private var startTime: Long = 0L
    private var timeoutJob: Job? = null

    private val _elapsedSeconds = MutableStateFlow(0f)
    val elapsedSeconds = _elapsedSeconds.asStateFlow()
    val totalSeconds get() = QUIZ_DURATION

    private val _questionNumber = MutableStateFlow(1)
    val questionNumber = _questionNumber.asStateFlow()

    val currentQuestion get() = currentQuiz!!.questions[_questionNumber.value - 1]

    private val _questionCount = MutableStateFlow(QUESTION_COUNT)
    val questionCount = _questionCount.asStateFlow()

    private val _buttonEnabled = MutableStateFlow(false)
    val buttonEnabled = _buttonEnabled.asStateFlow()

    private val _optionsEnabled = MutableStateFlow(true)
    val optionsEnabled = _optionsEnabled.asStateFlow()

    private val _answerOptions = MutableStateFlow<List<AnswerOptionState>>(emptyList())
    val answerOptions = _answerOptions.asStateFlow()

    private var currentQuiz: Quiz? = null
    private var selectedOptionIndex = -1
    private val answers = mutableListOf<Int>()


    fun loadQuiz(category: Category, difficulty: Difficulty) {
        viewModelScope.launch {
            Timber.i("Loading quiz ($category, $difficulty)")
            try {
                // Prepare quiz
                abortQuiz()
                val quiz = quizRepository.fetchNewQuiz(5, category, difficulty)
                currentQuiz = quiz

                // Start timer
                startTime = System.currentTimeMillis()
                timeoutJob = viewModelScope.launch {
                    Timber.i("Starting quiz timer")
                    while (_elapsedSeconds.value <= QUIZ_DURATION) {
                        _elapsedSeconds.value = (System.currentTimeMillis() - startTime) / 1000f
                        delay(TIMER_INTERVAL)
                    }
                    timeout()
                }

                // Update state
                _buttonEnabled.value = true
                _optionsEnabled.value = true
                _answerOptions.value = quiz.questions[0].options.map {
                    AnswerOptionState(it, RadioButtonState.Off)
                }
                _state.value = QuizScreenUiState.Loaded
            } catch (e: Exception) {
                _state.value = QuizScreenUiState.Error
            }
        }
    }

    private fun timeout() {
        Timber.i("Quiz timeout")

        // Record random incorrect answers as per the specification
        val incorrectCount = QUESTION_COUNT - answers.size
        Timber.i("Saving $incorrectCount incorrect answers")
        repeat(incorrectCount) {
            val choices = currentQuestion.options.indices.toMutableList()
            choices -= currentQuestion.correctOptionIndex
            answers += choices.random()
        }
        Timber.i("Total answers: ${answers.size}")

        viewModelScope.launch {
            quizRepository.saveQuizResults(CompletedQuiz(currentQuiz!!, answers.toList()))
            answers.clear()
            _state.value = QuizScreenUiState.Timeout
        }
    }

    fun advanceQuiz() {
        check(selectedOptionIndex >= 0)

        answers += selectedOptionIndex
        revealAnswer()

        viewModelScope.launch {
            delay(REVEAL_DURATION)
            if (questionNumber.value == currentQuiz!!.questions.size) {
                endQuiz()
            } else nextQuestion()
        }
    }

    private fun revealAnswer() {
        Timber.i("Revealing answer")
        check(answers.size >= _questionNumber.value)

        val newOptions = mutableListOf<AnswerOptionState>()
        for (i in _answerOptions.value.indices) {
            val option = _answerOptions.value[i]

            @Suppress("CascadeIf") // 'if' is explicitly ordered unlike 'when'
            newOptions += option.copy(
                buttonState = if (i == currentQuestion.correctOptionIndex) {
                    // The correct option is always correct, regardless of whether it is selected.
                    RadioButtonState.Correct
                } else if (i == selectedOptionIndex) {
                    // A selected option that is not correct is incorrect (duh.)
                    RadioButtonState.Incorrect
                } else RadioButtonState.Off // unselected option
            )
        }

        _answerOptions.value = newOptions
        selectedOptionIndex = -1
        _optionsEnabled.value = false
        _buttonEnabled.value = false
    }

    private fun endQuiz() {
        Timber.i("Ending quiz")
        timeoutJob?.cancel()
        viewModelScope.launch {
            quizRepository.saveQuizResults(CompletedQuiz(currentQuiz!!, answers.toList()))
            answers.clear()
            _state.value = QuizScreenUiState.Results
        }
    }

    private fun nextQuestion() {
        Timber.i("Next question")
        _optionsEnabled.value = true
        _buttonEnabled.value = true
        _buttonEnabled.value = false
        _questionNumber.value++
        _answerOptions.value = currentQuestion.options.map {
            AnswerOptionState(it, RadioButtonState.Off)
        }
    }

    fun onOptionSelected(index: Int) {
        Timber.i("Selecting option $index")
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

    fun abortQuiz() {
        Timber.i("Aborting quiz")
        timeoutJob?.cancel()
        currentQuiz = null
        answers.clear()
        selectedOptionIndex = -1
        _elapsedSeconds.value = 0f
        _questionNumber.value = 1
        _state.value = QuizScreenUiState.Loading
    }
}
