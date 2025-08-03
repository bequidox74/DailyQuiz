package ru.trfx.dailyquiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.HistoryScreenDestination
import com.ramcosta.composedestinations.generated.destinations.QuizStartScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import ru.trfx.dailyquiz.ui.ScreenTransitions
import ru.trfx.dailyquiz.ui.component.Button
import ru.trfx.dailyquiz.ui.theme.Colors
import ru.trfx.dailyquiz.ui.theme.DailyQuizTheme
import ru.trfx.dailyquiz.ui.theme.Shapes

@Destination<RootGraph>(start = true, style = ScreenTransitions::class)
@Composable
fun WelcomeScreen(
    navigator: DestinationsNavigator,
) {
    WelcomeScreenContent(
        onStart = {
            navigator.navigate(QuizStartScreenDestination)
        },
        onHistory = {
            navigator.navigate(HistoryScreenDestination)
        }
    )
}

@Composable
private fun WelcomeScreenContent(
    onStart: () -> Unit,
    onHistory: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 80.dp)
            .fillMaxWidth()
            .wrapContentSize()
            .clip(Shapes.Round)
            .clickable(onClick = onHistory)
            .background(color = Colors.White, shape = Shapes.Round)
            .height(42.dp)
            .padding(horizontal = 12.dp),
    ) {
        Text(
            text = stringResource(R.string.screen_history),
            color = Colors.Blue,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Icon(
            painter = painterResource(R.drawable.ic_history),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentSize()
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.img_logo),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(42.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(color = Colors.White, shape = Shapes.Card)
                .padding(24.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.text_welcome),
                color = Colors.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(42.dp))
            Button(
                text = stringResource(R.string.button_start_quiz).uppercase(),
                onClick = onStart,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    DailyQuizTheme {
        WelcomeScreenContent(
            onStart = {},
            onHistory = {},
        )
    }
}
