package ru.trfx.dailyquiz.quiz.repository

import ru.trfx.dailyquiz.quiz.Category
import ru.trfx.dailyquiz.quiz.Difficulty
import ru.trfx.dailyquiz.quiz.Quiz

interface QuizRepository {
    suspend fun getQuiz(
        questionCount: Int,
        category: Category?,
        difficulty: Difficulty?,
    ): Quiz
}
