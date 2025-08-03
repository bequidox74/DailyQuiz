package ru.trfx.dailyquiz.quiz.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.WelcomeScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.quiz.QuizHandler
import ru.trfx.dailyquiz.ui.component.AnswerOptionState
import ru.trfx.dailyquiz.ui.component.HistoryAnswerCard
import ru.trfx.dailyquiz.ui.component.RadioButtonState
import ru.trfx.dailyquiz.ui.component.ResultsCard

@Destination<RootGraph>
@Composable
fun QuizResultsScreen(navigator: DestinationsNavigator) {
    val quiz = QuizHandler.currentQuiz!!
    val answers = QuizHandler.answers.toList()

    var stars = 0
    for (i in quiz.questions.indices) {
        val question = quiz.questions[i]
        if (answers[i] == question.correctOptionIndex) stars++
    }

    TemplateQuizScreen(
        title = stringResource(R.string.title_results),
        showBackButton = false,
        onBackButtonClick = {},
    ) {
        ResultsCard(
            stars = stars,
            onRestart = { navigator.navigate(WelcomeScreenDestination) },
        )

        for (i in quiz.questions.indices) {
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
    }
}
