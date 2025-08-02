package ru.trfx.dailyquiz.quiz.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.WelcomeScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.quiz.QuizHandler
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
    }
}
