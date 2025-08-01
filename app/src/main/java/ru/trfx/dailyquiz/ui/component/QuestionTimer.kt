package ru.trfx.dailyquiz.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.trfx.dailyquiz.ui.theme.Colors
import java.util.Locale

@Composable
fun QuestionTimer(
    elapsedSeconds: Float,
    totalSeconds: Float,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        Row {
            Text(
                text = elapsedSecondsToString(elapsedSeconds),
                fontSize = 12.sp,
                color = Colors.Purple,
            )
            Spacer(Modifier.weight(1.0f))
            Text(
                text = elapsedSecondsToString(totalSeconds),
                fontSize = 12.sp,
                color = Colors.Purple,
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        ) {
            val radius = CornerRadius(size.height)
            drawRoundRect(
                color = Colors.Silver,
                cornerRadius = radius,
            )
            drawRoundRect(
                color = Colors.Purple,
                cornerRadius = radius,
                size = Size(width = size.width * elapsedSeconds / totalSeconds, height = size.height),
            )
        }
    }
}

fun elapsedSecondsToString(elapsedSeconds: Float): String {
    val truncated = elapsedSeconds.toInt()
    val minutes = truncated / 60
    val seconds = truncated % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}

@Preview
@Composable
private fun PreviewTimer() {
    QuestionTimer(
        elapsedSeconds = 150f,
        totalSeconds = 300f,
    )
}
