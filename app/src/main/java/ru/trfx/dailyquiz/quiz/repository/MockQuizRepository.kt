package ru.trfx.dailyquiz.quiz.repository

import kotlinx.coroutines.delay
import ru.trfx.dailyquiz.quiz.Category
import ru.trfx.dailyquiz.quiz.Difficulty
import ru.trfx.dailyquiz.quiz.Question
import ru.trfx.dailyquiz.quiz.Quiz

class MockQuizRepository : QuizRepository {
    override suspend fun getQuiz(
        questionCount: Int,
        category: Category?,
        difficulty: Difficulty?,
    ): Quiz {
        delay(400)
        return Quiz(
            questions = listOf(
                Question(
                    title = "Lorem ipsum A?",
                    options = listOf("A", "B", "C", "D"),
                    correctOptionIndex = 0,
                ),
                Question(
                    title = "Lorem ipsum B?",
                    options = listOf("A", "B", "C", "D"),
                    correctOptionIndex = 1,
                ),
                Question(
                    title = "Lorem ipsum C?",
                    options = listOf("A", "B", "C", "D"),
                    correctOptionIndex = 2,
                ),
            ),
            category = null,
            difficulty = Difficulty.Medium,
        )
    }
}
