package ru.trfx.dailyquiz.quiz.repository

import android.text.Html
import androidx.core.text.HtmlCompat
import kotlinx.serialization.json.Json
import ru.trfx.dailyquiz.history.QuizHistoryDao
import ru.trfx.dailyquiz.history.QuizHistoryEntry
import ru.trfx.dailyquiz.quiz.Category
import ru.trfx.dailyquiz.quiz.CompletedQuiz
import ru.trfx.dailyquiz.quiz.Difficulty
import ru.trfx.dailyquiz.quiz.NetworkQuizFetcher
import ru.trfx.dailyquiz.quiz.Question
import ru.trfx.dailyquiz.quiz.Quiz
import ru.trfx.dailyquiz.quiz.QuizFetchResponse
import timber.log.Timber
import kotlin.random.Random

class DefaultQuizRepository(
    private val fetcher: NetworkQuizFetcher,
    private val historyDao: QuizHistoryDao,
) : QuizRepository {
    private var _lastQuiz: CompletedQuiz? = null
    override val lastQuiz get() = _lastQuiz

    override suspend fun fetchNewQuiz(
        questionCount: Int,
        category: Category,
        difficulty: Difficulty,
    ): Quiz {
        Timber.i("Fetching new quiz ($category, $difficulty)")
        val response = fetcher.fetchQuiz(
            amount = questionCount,
            category = category.categoryId,
            difficulty = difficulty.apiName,
        )
        return response.toModel(category, difficulty)
    }

    private fun QuizFetchResponse.toModel(category: Category, difficulty: Difficulty): Quiz {
        val questions = mutableListOf<Question>()

        for (result in results) {
            val options = result.incorrect_answers.toMutableList()
            options.shuffle()

            val correctIndex = Random.nextInt(0, options.size)
            options.add(correctIndex, result.correct_answer)

            questions += Question(
                title = Html.fromHtml(result.question, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
                options = options,
                correctOptionIndex = correctIndex,
            )
        }

        return Quiz(
            questions = questions,
            category = category,
            difficulty = difficulty,
        )
    }

    override suspend fun saveQuizResults(quiz: CompletedQuiz): CompletedQuiz {
        Timber.i("Saving results")
        val json = Json.encodeToString(quiz)
        val id = historyDao.save(QuizHistoryEntry(QuizHistoryEntry.UNSET_ID, json))
        Timber.i("New ID: $id")

        val withId = quiz.copy(id = id.toInt())
        _lastQuiz = withId
        return withId
    }

    override suspend fun getQuizHistory(): List<CompletedQuiz> {
        Timber.i("Retrieving quiz history")
        val result = historyDao.getAll().map { it.toModel() }
        Timber.i("Retrieved ${result.size} entries")
        return result
    }

    private fun QuizHistoryEntry.toModel(): CompletedQuiz {
        return Json.decodeFromString<CompletedQuiz>(json).copy(id = id)
    }

    override suspend fun getSavedQuiz(id: Int): CompletedQuiz? {
        Timber.i("Getting quiz id=$id")
        val result = historyDao.findById(id)?.toModel()
        Timber.i("Found: ${result != null}")
        return result
    }

    override suspend fun deleteSavedQuiz(id: Int) {
        Timber.i("Deleting quiz id=$id")
        historyDao.deleteById(id)
    }
}
