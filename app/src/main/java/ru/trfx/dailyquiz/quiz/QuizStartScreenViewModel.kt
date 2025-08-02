package ru.trfx.dailyquiz.quiz

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class QuizStartScreenViewModel : ViewModel() {
    val categories: List<Category> = Category.entries
    val selectedCategoryIndex = MutableStateFlow(-1)

    val difficulties: List<Difficulty> = Difficulty.entries
    val selectedDifficultyIndex = MutableStateFlow(-1)

    fun startQuiz() {
        TODO("Not yet implemented")
    }
}
