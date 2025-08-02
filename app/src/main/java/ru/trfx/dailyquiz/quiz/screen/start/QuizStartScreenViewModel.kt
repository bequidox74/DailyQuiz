package ru.trfx.dailyquiz.quiz.screen.start

import androidx.lifecycle.ViewModel

sealed interface QuizStartScreenUiState {
    data class Setup(
        var selectedCategory: Int,
        var selectedDifficulty: Int,
    ) : QuizStartScreenUiState

    data object Loading : QuizStartScreenUiState
}

class QuizStartScreenViewModel : ViewModel() {
    fun startQuiz() {
        TODO("Not yet implemented")
    }
}
