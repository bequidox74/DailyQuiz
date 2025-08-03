package ru.trfx.dailyquiz.quiz.screen

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.QuizResultsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.WelcomeScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.quiz.Category
import ru.trfx.dailyquiz.quiz.Difficulty
import ru.trfx.dailyquiz.ui.component.QuestionCard
import ru.trfx.dailyquiz.ui.component.Throbber
import ru.trfx.dailyquiz.ui.theme.Colors

@Destination<RootGraph>
@Composable
fun QuizScreen(
    category: Category,
    difficulty: Difficulty,
    navigator: DestinationsNavigator,
    viewModel: QuizScreenViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.loadQuiz(category, difficulty)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state) {
        QuizScreenUiState.Error -> {
            Toast.makeText(LocalContext.current, R.string.toast_error, Toast.LENGTH_SHORT).show()
            navigator.navigate(WelcomeScreenDestination)
        }

        QuizScreenUiState.Results -> {
            navigator.navigate(QuizResultsScreenDestination)
        }

        QuizScreenUiState.Loading -> {
            Throbber(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
            )
        }

        QuizScreenUiState.Loaded -> {
            val questionNumber by viewModel.questionNumber.collectAsStateWithLifecycle()
            val answerOptions by viewModel.answerOptions.collectAsStateWithLifecycle()
            val buttonEnabled by viewModel.buttonEnabled.collectAsStateWithLifecycle()
            val optionsEnabled by viewModel.optionsEnabled.collectAsStateWithLifecycle()
            val questionCount by viewModel.questionCount.collectAsStateWithLifecycle()

            val realElapsedSeconds by viewModel.elapsedSeconds.collectAsStateWithLifecycle()
            val elapsedSeconds by animateFloatAsState(realElapsedSeconds)

            TemplateQuizScreen(
                title = null,
                showBackButton = true,
                onBackButtonClick = { navigator.navigate(WelcomeScreenDestination) }
            ) {
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
