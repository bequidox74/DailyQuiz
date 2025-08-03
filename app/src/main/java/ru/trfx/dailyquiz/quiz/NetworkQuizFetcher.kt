package ru.trfx.dailyquiz.quiz

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

@Suppress("PropertyName")
@Serializable
data class QuizFetchResponse(
    val response_code: Int,
    val results: List<QuizFetchResult>,
)

@Suppress("PropertyName")
@Serializable
data class QuizFetchResult(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>,
)

interface NetworkQuizFetcher {
    @GET("api.php?type=multiple")
    suspend fun fetchQuiz(
        @Query("amount") amount: Int,
        @Query("category") category: Int?,
        @Query("difficulty") difficulty: String?,
    ): QuizFetchResponse
}
