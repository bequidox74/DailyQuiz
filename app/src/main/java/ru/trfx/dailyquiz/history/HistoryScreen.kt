package ru.trfx.dailyquiz.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.quiz.BaseQuizScreen

@Destination<RootGraph>
@Composable
fun HistoryScreen() {
    BaseQuizScreen(
        title = stringResource(R.string.screen_history),
        showBackButton = false,
        onBackButtonClick = {},
    ) { }
}
