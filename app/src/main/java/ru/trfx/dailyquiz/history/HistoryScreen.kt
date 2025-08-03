package ru.trfx.dailyquiz.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.koin.compose.koinInject
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.quiz.CompletedQuiz
import ru.trfx.dailyquiz.quiz.repository.QuizRepository
import ru.trfx.dailyquiz.quiz.screen.TemplateQuizScreen
import ru.trfx.dailyquiz.ui.component.QuizHistoryCard
import java.time.LocalDateTime
import java.time.ZoneId

@Destination<RootGraph>
@Composable
fun HistoryScreen() {
    val quizRepository = koinInject<QuizRepository>()
    var history by remember { mutableStateOf<List<CompletedQuiz>?>(null) }

    LaunchedEffect(Unit) {
        history = quizRepository.getQuizHistory()
    }

    TemplateQuizScreen(
        title = stringResource(R.string.screen_history),
        showBackButton = false,
        onBackButtonClick = {},
    ) {
        if (history == null) return
        @Suppress("NAME_SHADOWING") val history = history!!

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            items(count = history.size, key = { history[it].id!! }) { quizIndex ->
                val completedQuiz = history[quizIndex]
                val quiz = completedQuiz.quiz

                var stars = 0
                for (i in quiz.questions.indices) {
                    val question = quiz.questions[i]
                    if (question.correctOptionIndex == completedQuiz.answers[i]) {
                        stars++
                    }
                }

                // Interpret the timestamp in the current time zone
                val dateTime = LocalDateTime.ofInstant(
                    completedQuiz.creationInstant,
                    ZoneId.systemDefault(),
                )

                QuizHistoryCard(
                    title = stringResource(R.string.title_history_card, completedQuiz.id!!),
                    stars = stars,
                    dateTime = dateTime,
                    category = quiz.category.let {
                        if (it == null) null else stringResource(it.resId)
                    },
                    difficulty = quiz.difficulty.let {
                        if (it == null) null else stringResource(it.resId)
                    },
                )
            }
        }
    }
}
