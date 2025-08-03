package ru.trfx.dailyquiz.quiz.repository

import ru.trfx.dailyquiz.quiz.Category
import ru.trfx.dailyquiz.quiz.CompletedQuiz
import ru.trfx.dailyquiz.quiz.Difficulty
import ru.trfx.dailyquiz.quiz.Quiz

interface QuizRepository {
    val lastQuiz: CompletedQuiz?

    suspend fun fetchNewQuiz(
        questionCount: Int,
        category: Category?,
        difficulty: Difficulty?,
    ): Quiz

    suspend fun saveQuizResults(quiz: CompletedQuiz)

    suspend fun getQuizHistory(): List<CompletedQuiz>
}
