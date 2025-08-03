package ru.trfx.dailyquiz.quiz.repository

import kotlinx.coroutines.delay
import ru.trfx.dailyquiz.quiz.Category
import ru.trfx.dailyquiz.quiz.CompletedQuiz
import ru.trfx.dailyquiz.quiz.Difficulty
import ru.trfx.dailyquiz.quiz.Question
import ru.trfx.dailyquiz.quiz.Quiz
import timber.log.Timber
import java.time.Instant

class MockQuizRepository : QuizRepository {
    private var _lastQuiz: CompletedQuiz? = null
    override val lastQuiz get() = _lastQuiz

    private val history = mutableListOf<CompletedQuiz>()
    private var lastId = 1

    init {
        repeat(5) {
            history += CompletedQuiz(
                quiz = Quiz(
                    questions = listOf(
                        Question(
                            title = "Lorem ipsum A?",
                            options = listOf("A", "B", "C", "D"),
                            correctOptionIndex = 0,
                        ),
                        Question(
                            title = "Lorem ipsum B?",
                            options = listOf("A", "B", "C", "D"),
                            correctOptionIndex = 0,
                        ),
                        Question(
                            title = "Lorem ipsum C?",
                            options = listOf("A", "B", "C", "D"),
                            correctOptionIndex = 0,
                        ),
                        Question(
                            title = "Lorem ipsum D?",
                            options = listOf("A", "B", "C", "D"),
                            correctOptionIndex = 0,
                        ),
                        Question(
                            title = "Lorem ipsum E?",
                            options = listOf("A", "B", "C", "D"),
                            correctOptionIndex = 0,
                        ),
                    ),
                    category = Category.Film,
                    difficulty = Difficulty.Easy,
                ),
                answers = listOf(0, 2, 1, 3, 0),
                id = lastId++,
                creationEpochSecond = Instant.now().epochSecond,
            )
        }
    }

    override suspend fun fetchNewQuiz(
        questionCount: Int,
        category: Category,
        difficulty: Difficulty,
    ): Quiz {
        Timber.i("Fetching mock quiz")
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
            category = category,
            difficulty = difficulty,
        )
    }

    override suspend fun saveQuizResults(quiz: CompletedQuiz): CompletedQuiz {
        Timber.i("Saving quiz results")
        require(quiz.id == null)
        delay(300)
        _lastQuiz = quiz
        val saved = quiz.copy(id = lastId++)
        history += saved
        return saved
    }

    override suspend fun getQuizHistory(): List<CompletedQuiz> {
        Timber.i("Retrieving quiz history (${history.size} entries)")
        delay(500)
        return history.toList()
    }

    override suspend fun getSavedQuiz(id: Int): CompletedQuiz? {
        Timber.i("Getting quiz id=$id")
        val result = history.find { it.id == id }
        Timber.i("found: ${result != null}")
        return result
    }

    override suspend fun deleteSavedQuiz(id: Int) {
        Timber.i("Deleting quiz id=$id")
        require(id != 0)
        val result = history.removeIf { it.id == id }
        Timber.i("deleted: $result")
    }
}
