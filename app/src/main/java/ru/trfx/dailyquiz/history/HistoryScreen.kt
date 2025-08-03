package ru.trfx.dailyquiz.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.QuizResultsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.QuizStartScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.quiz.CompletedQuiz
import ru.trfx.dailyquiz.quiz.repository.QuizRepository
import ru.trfx.dailyquiz.quiz.screen.TemplateQuizScreen
import ru.trfx.dailyquiz.ui.component.Button
import ru.trfx.dailyquiz.ui.component.Card
import ru.trfx.dailyquiz.ui.component.QuizHistoryCard
import ru.trfx.dailyquiz.ui.component.Throbber
import ru.trfx.dailyquiz.ui.theme.Colors
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId

@Destination<RootGraph>
@Composable
fun HistoryScreen(navigator: DestinationsNavigator) {
    val scope = rememberCoroutineScope()
    val quizRepository = koinInject<QuizRepository>()
    var history by remember { mutableStateOf<List<CompletedQuiz>?>(null) }

    var selectedId by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            text = { Text(text = stringResource(R.string.dialog_delete_quiz)) },
            onDismissRequest = { showDialog = false },
            containerColor = Colors.White,
            textContentColor = Colors.Black,

            confirmButton = {
                TextButton(
                    onClick = {
                        Timber.i("confirm")
                        showDialog = false
                        scope.launch {
                            quizRepository.deleteSavedQuiz(selectedId)
                            history = quizRepository.getQuizHistory()
                            Timber.i("${history!!.size}")
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.button_yes),
                        color = Colors.Blue,
                        fontWeight = FontWeight.Medium,
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        Timber.i("dismiss")
                        showDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.button_no),
                        color = Colors.Blue,
                        fontWeight = FontWeight.Medium,
                    )
                }
            },
        )
    }

    LaunchedEffect(Unit) {
        Timber.i("initial load")
        history = quizRepository.getQuizHistory()
    }

    TemplateQuizScreen(
        title = stringResource(R.string.screen_history),
        showBackButton = false,
        onBackButtonClick = {},
    ) {
        if (history == null) {
            Throbber()
            return
        }

        @Suppress("NAME_SHADOWING") val history = history!!

        if (history.isEmpty()) {
            Card {
                Text(
                    text = stringResource(R.string.text_no_quizzes),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(48.dp))
                Button(
                    text = stringResource(R.string.button_start_quiz).uppercase(),
                    onClick = { navigator.navigate(QuizStartScreenDestination) },
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                items(count = history.size, key = { history[it].id!! }) { quizIndex ->
                    Timber.i("recomposing")
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

                        onClick = {
                            navigator.navigate(
                                QuizResultsScreenDestination(quizId = completedQuiz.id),
                            )
                        },

                        onLongClick = {
                            Timber.i("long click")
                            selectedId = completedQuiz.id
                            showDialog = true
                        }
                    )
                }
            }
        }
    }
}
