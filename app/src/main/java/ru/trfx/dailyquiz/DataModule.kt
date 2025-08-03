package ru.trfx.dailyquiz

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.trfx.dailyquiz.history.QuizHistoryDao
import ru.trfx.dailyquiz.history.QuizHistoryEntry
import ru.trfx.dailyquiz.quiz.NetworkQuizFetcher
import ru.trfx.dailyquiz.quiz.repository.DefaultQuizRepository
import ru.trfx.dailyquiz.quiz.repository.QuizRepository

@Database(entities = [QuizHistoryEntry::class], version = 1)
internal abstract class DailyQuizDatabase : RoomDatabase() {
    abstract fun quizHistoryDao(): QuizHistoryDao
}

internal val dataModule = module {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(
            Json.asConverterFactory(
                "application/json; charset=UTF8".toMediaType(),
            )
        )
        .build()

    single<Retrofit> { retrofit }
    single<NetworkQuizFetcher> { retrofit.create(NetworkQuizFetcher::class.java) }

    single<DailyQuizDatabase> {
        Room.databaseBuilder(
            context = get(),
            klass = DailyQuizDatabase::class.java,
            name = "database"
        ).build()
    }
    single<QuizHistoryDao> { get<DailyQuizDatabase>().quizHistoryDao() }

//    singleOf(::MockQuizRepository) bind QuizRepository::class
    singleOf(::DefaultQuizRepository) bind QuizRepository::class
}
