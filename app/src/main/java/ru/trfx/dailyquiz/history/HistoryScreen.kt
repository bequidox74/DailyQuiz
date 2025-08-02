package ru.trfx.dailyquiz.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.quiz.screen.TemplateQuizScreen

@Destination<RootGraph>
@Composable
fun HistoryScreen() {
    TemplateQuizScreen(
        title = stringResource(R.string.screen_history),
        showBackButton = false,
        onBackButtonClick = {},
    ) { }
}
