package ru.trfx.dailyquiz.quiz.repository

import ru.trfx.dailyquiz.quiz.Category
import ru.trfx.dailyquiz.quiz.CompletedQuiz
import ru.trfx.dailyquiz.quiz.Difficulty
import ru.trfx.dailyquiz.quiz.Quiz

interface QuizRepository {
    var lastQuiz: CompletedQuiz?

    suspend fun getQuiz(
        questionCount: Int,
        category: Category?,
        difficulty: Difficulty?,
    ): Quiz
}
