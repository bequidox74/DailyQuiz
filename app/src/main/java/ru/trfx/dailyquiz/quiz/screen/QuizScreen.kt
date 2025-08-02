package ru.trfx.dailyquiz.quiz.screen

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.WelcomeScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.quiz.Category
import ru.trfx.dailyquiz.quiz.Difficulty
import ru.trfx.dailyquiz.quiz.QuizHandler
import ru.trfx.dailyquiz.ui.component.QuestionCard
import ru.trfx.dailyquiz.ui.component.Throbber

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
            TODO()
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
            val question = QuizHandler.getQuestion(questionNumber)

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
                    questionCount = QuizHandler.questionCount,
                    title = question.title,
                    options = answerOptions,
                    onOptionClick = viewModel::onOptionSelected,
                    optionsEnabled = optionsEnabled,
                    buttonText = stringResource(
                        if (questionNumber < QuizHandler.questionCount) {
                            R.string.button_next
                        } else R.string.button_end_quiz
                    ).uppercase(),
                    buttonEnabled = buttonEnabled,
                    onButtonClick = viewModel::advanceQuiz,
                )
            }
        }
    }
}
