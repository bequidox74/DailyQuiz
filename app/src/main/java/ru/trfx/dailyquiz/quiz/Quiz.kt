package ru.trfx.dailyquiz.quiz

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import ru.trfx.dailyquiz.R
import java.time.Instant

enum class Difficulty(@StringRes val resId: Int) {
    Any(R.string.difficulty_any),
    Easy(R.string.difficulty_easy),
    Medium(R.string.difficulty_medium),
    Hard(R.string.difficulty_hard),
}

enum class Category(@StringRes val resId: Int, val categoryId: Int) {
    Any(R.string.category_any, -1),
    GeneralKnowledge(R.string.category_general_knowledge, 9),
    Books(R.string.category_books, 10),
    Film(R.string.category_film, 11),
    Music(R.string.category_music, 12),
    MusicalsAndTheaters(R.string.category_musicals, 13),
    Television(R.string.category_television, 14),
    VideoGames(R.string.category_video_games, 15),
    BoardGames(R.string.category_board_games, 16),
    ScienceAndNature(R.string.category_science, 17),
    Computers(R.string.category_computers, 18),
    Mathematics(R.string.category_mathematics, 19),
    Mythology(R.string.category_mythology, 20),
    Sports(R.string.category_sports, 21),
    Geography(R.string.category_geography, 22),
    History(R.string.category_history, 23),
    Politics(R.string.category_politics, 24),
    Art(R.string.category_art, 25),
    Celebrities(R.string.category_celebrities, 26),
    Animals(R.string.category_animals, 27),
    Vehicles(R.string.category_vehicles, 28),
    Comics(R.string.category_comics, 29),
    Gadgets(R.string.category_gadgets, 30),
    JapaneseAnimeAndManga(R.string.category_anime, 31),
    CartoonsAndAnimation(R.string.category_cartoons, 32),
}

@Immutable
data class Quiz(
    val questions: List<Question>,
    val category: Category?,
    val difficulty: Difficulty?,
)

@Immutable
data class Question(
    val title: String,
    val options: List<String>,
    val correctOptionIndex: Int,
)

@Immutable
data class CompletedQuiz(
    val quiz: Quiz,
    val answers: List<Int>,
    val creationInstant: Instant = Instant.now(),
    val id: Int? = null,
) {
    init {
        require(quiz.questions.size == answers.size)
    }
}
