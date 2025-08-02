package ru.trfx.dailyquiz.quiz

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizStartScreenViewModel : ViewModel() {
    val categories: List<Category> = Category.entries
    private val _selectedCategoryIndex = MutableStateFlow(-1)
    val selectedCategoryIndex = _selectedCategoryIndex.asStateFlow()

    val difficulties: List<Difficulty> = Difficulty.entries
    private val _selectedDifficultyIndex = MutableStateFlow(-1)
    val selectedDifficultyIndex = _selectedDifficultyIndex.asStateFlow()
}
