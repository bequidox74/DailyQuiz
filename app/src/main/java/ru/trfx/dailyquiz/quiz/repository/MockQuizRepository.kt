package ru.trfx.dailyquiz.quiz.repository

import kotlinx.coroutines.delay
import ru.trfx.dailyquiz.quiz.Category
import ru.trfx.dailyquiz.quiz.CompletedQuiz
import ru.trfx.dailyquiz.quiz.Difficulty
import ru.trfx.dailyquiz.quiz.Question
import ru.trfx.dailyquiz.quiz.Quiz
import timber.log.Timber

class MockQuizRepository : QuizRepository {
    private var _lastQuiz: CompletedQuiz? = null
    override val lastQuiz get() = _lastQuiz

    private val history = mutableListOf<CompletedQuiz>()

    init {
        history += CompletedQuiz(
            quiz = Quiz(
                questions = listOf(
                    Question(
                        title = "Lorem ipsum X?",
                        options = listOf("A", "B", "C", "D"),
                        correctOptionIndex = 0,
                    ),
                    Question(
                        title = "Lorem ipsum Y?",
                        options = listOf("A", "B", "C", "D"),
                        correctOptionIndex = 1,
                    ),
                    Question(
                        title = "Lorem ipsum Z?",
                        options = listOf("A", "B", "C", "D"),
                        correctOptionIndex = 2,
                    ),
                ),
                category = Category.Film,
                difficulty = Difficulty.Easy,
            ),
            answers = listOf(0, 2, 1),
        )
    }

    override suspend fun fetchNewQuiz(
        questionCount: Int,
        category: Category?,
        difficulty: Difficulty?,
    ): Quiz {
        Timber.d("Fetching mock quiz")
        delay(700)
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
                Question(
                    title = "Lorem ipsum D?",
                    options = listOf("A", "B", "C", "D"),
                    correctOptionIndex = 3,
                ),
                Question(
                    title = "Lorem ipsum E?",
                    options = listOf("A", "B", "C", "D"),
                    correctOptionIndex = 0,
                ),
            ),
            category = null,
            difficulty = Difficulty.Medium,
        )
    }

    override suspend fun saveQuizResults(quiz: CompletedQuiz) {
        Timber.i("Saving quiz results")
        delay(300)
        _lastQuiz = quiz
        history += quiz
    }

    override suspend fun getQuizHistory(): List<CompletedQuiz> {
        Timber.i("Retrieving quiz history")
        delay(500)
        return history
    }
}
