package ru.trfx.dailyquiz.history

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class QuizHistoryEntry(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val json: String,
) {
    companion object {
        const val UNSET_ID = 0
    }
}

@Dao
interface QuizHistoryDao {
    @Query("SELECT * FROM QuizHistoryEntry")
    suspend fun getAll(): List<QuizHistoryEntry>

    @Insert
    suspend fun save(entry: QuizHistoryEntry): Long

    @Query("SELECT * FROM QuizHistoryEntry WHERE id = :id")
    suspend fun findById(id: Int): QuizHistoryEntry?

    @Query("DELETE FROM QuizHistoryEntry WHERE id = :id")
    suspend fun deleteById(id: Int)
}
