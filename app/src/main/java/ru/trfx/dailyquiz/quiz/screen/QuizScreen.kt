package ru.trfx.dailyquiz.quiz.screen

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.QuizResultsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.QuizStartScreenDestination
import com.ramcosta.composedestinations.generated.destinations.WelcomeScreenDestination
import com.ramcosta.composedestinations.generated.navgraphs.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.quiz.Category
import ru.trfx.dailyquiz.quiz.Difficulty
import ru.trfx.dailyquiz.ui.ScreenTransitions
import ru.trfx.dailyquiz.ui.component.Button
import ru.trfx.dailyquiz.ui.component.Card
import ru.trfx.dailyquiz.ui.component.QuestionCard
import ru.trfx.dailyquiz.ui.component.Throbber
import ru.trfx.dailyquiz.ui.theme.Colors

@Destination<RootGraph>(style = ScreenTransitions::class)
@Composable
fun QuizScreen(
    category: Category,
    difficulty: Difficulty,
    navigator: DestinationsNavigator,
    viewModel: QuizScreenViewModel = koinViewModel(),
) {
    DisposableEffect(Unit) {
        viewModel.loadQuiz(category, difficulty)
        onDispose { viewModel.abortQuiz() }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state) {
        QuizScreenUiState.Error -> {
            Toast.makeText(LocalContext.current, R.string.toast_error, Toast.LENGTH_SHORT).show()
            navigator.navigate(WelcomeScreenDestination) {
                popUpTo(RootNavGraph.startRoute)
            }
        }

        QuizScreenUiState.Results -> {
            navigator.navigate(QuizResultsScreenDestination()) {
                popUpTo(RootNavGraph.startRoute)
            }
        }

        QuizScreenUiState.Loading -> {
            Throbber(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
            )
        }

        else -> {
            val questionNumber by viewModel.questionNumber.collectAsStateWithLifecycle()
            val answerOptions by viewModel.answerOptions.collectAsStateWithLifecycle()
            val buttonEnabled by viewModel.buttonEnabled.collectAsStateWithLifecycle()
            val optionsEnabled by viewModel.optionsEnabled.collectAsStateWithLifecycle()
            val questionCount by viewModel.questionCount.collectAsStateWithLifecycle()

            val realElapsedSeconds by viewModel.elapsedSeconds.collectAsStateWithLifecycle()
            val elapsedSeconds by animateFloatAsState(realElapsedSeconds)

            if (state == QuizScreenUiState.Timeout) {
                Dialog(onDismissRequest = {}) {
                    Card {
                        Text(
                            text = stringResource(R.string.title_timeout),
                            color = Colors.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.text_timeout),
                            color = Colors.Black,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(48.dp))
                        Button(
                            text = stringResource(R.string.button_restart).uppercase(),
                            onClick = {
                                navigator.navigate(QuizStartScreenDestination) {
                                    popUpTo(RootNavGraph.startRoute)
                                }
                            },
                        )
                    }
                }
            }

            TemplateQuizScreen(
                title = null,
                showBackButton = true,
                onBackButtonClick = {
                    navigator.navigate(WelcomeScreenDestination) {
                        popUpTo(RootNavGraph.startRoute)
                    }
                }
            ) {
                LazyColumn {
                    item {
                        QuestionCard(
                            elapsedSeconds = elapsedSeconds,
                            totalSeconds = viewModel.totalSeconds,
                            currentQuestionNumber = questionNumber,
                            questionCount = questionCount,
                            title = viewModel.currentQuestion.title,
                            options = answerOptions,
                            onOptionClick = viewModel::onOptionSelected,
                            optionsEnabled = optionsEnabled,
                            buttonText = stringResource(
                                if (questionNumber < questionCount) {
                                    R.string.button_next
                                } else R.string.button_end_quiz
                            ).uppercase(),
                            buttonEnabled = buttonEnabled,
                            onButtonClick = viewModel::advanceQuiz,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.text_no_going_back),
                            color = Colors.White,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}
