package ru.trfx.dailyquiz.quiz.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.WelcomeScreenDestination
import com.ramcosta.composedestinations.generated.navgraphs.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.compose.koinInject
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.quiz.CompletedQuiz
import ru.trfx.dailyquiz.quiz.repository.QuizRepository
import ru.trfx.dailyquiz.ui.ScreenTransitions
import ru.trfx.dailyquiz.ui.component.AnswerOptionState
import ru.trfx.dailyquiz.ui.component.Button
import ru.trfx.dailyquiz.ui.component.HistoryAnswerCard
import ru.trfx.dailyquiz.ui.component.RadioButtonState
import ru.trfx.dailyquiz.ui.component.ResultsCard
import ru.trfx.dailyquiz.ui.theme.Colors

@Destination<RootGraph>(style = ScreenTransitions::class)
@Composable
fun QuizResultsScreen(
    navigator: DestinationsNavigator,
    quizId: Int? = null,
) {
    val quizRepository = koinInject<QuizRepository>()
    var completedQuiz by remember { mutableStateOf<CompletedQuiz?>(null) }

    LaunchedEffect(Unit) {
        completedQuiz = if (quizId == null) {
            quizRepository.lastQuiz!!
        } else {
            quizRepository.getSavedQuiz(quizId)
        }
    }


    TemplateQuizScreen(
        title = stringResource(R.string.title_results),
        showBackButton = false,
        onBackButtonClick = {},
    ) {
        if (completedQuiz == null) return@TemplateQuizScreen
        @Suppress("NAME_SHADOWING") val completedQuiz = completedQuiz!!

        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            val quiz = completedQuiz.quiz
            val answers = completedQuiz.answers

            var stars = 0
            for (i in quiz.questions.indices) {
                val question = quiz.questions[i]
                if (answers[i] == question.correctOptionIndex) stars++
            }

            item {
                Text(
                    text = stringResource(
                        R.string.quiz_category,
                        stringResource(quiz.category.resId),
                    ),
                    color = Colors.White,
                    fontSize = 16.sp,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        R.string.quiz_difficulty,
                        stringResource(quiz.difficulty.resId),
                    ),
                    color = Colors.White,
                    fontSize = 16.sp,
                )
            }

            item {
                Spacer(Modifier.height(24.dp))
                ResultsCard(
                    stars = stars,
                    onRestart = {
                        navigator.navigate(WelcomeScreenDestination) {
                            popUpTo(RootNavGraph.startRoute)
                        }
                    },
                    showButton = quizId == null,
                )
            }

            items(count = quiz.questions.size, key = { it }) { i ->
                val question = quiz.questions[i]
                val answer = answers[i]

                val options = question.options.mapIndexed { j, text ->
                    @Suppress("CascadeIf")
                    AnswerOptionState(
                        text = text,
                        buttonState = if (j == question.correctOptionIndex) {
                            RadioButtonState.Correct
                        } else if (j == answer) {
                            RadioButtonState.Incorrect
                        } else RadioButtonState.Off
                    )
                }

                Spacer(Modifier.height(24.dp))
                HistoryAnswerCard(
                    questionNumber = i + 1,
                    questionCount = quiz.questions.size,
                    title = question.title,
                    state = if (answer == question.correctOptionIndex) {
                        RadioButtonState.Correct
                    } else {
                        RadioButtonState.Incorrect
                    },
                    options = options,
                )
            }

            item {
                if (quizId == null) {
                    Spacer(Modifier.height(24.dp))
                    Button(
                        text = stringResource(R.string.button_restart).uppercase(),
                        onClick = {
                            navigator.navigate(WelcomeScreenDestination) {
                                popUpTo(RootNavGraph.startRoute)
                            }
                        },
                        invertColors = true,
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
