package ru.trfx.dailyquiz.ui.component

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ru.trfx.dailyquiz.R

@Composable
fun Throbber(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "throbber")
    val degree by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "throbber"
    )
    Image(
        painter = painterResource(R.drawable.img_throbber),
        contentDescription = null,
        modifier = modifier.rotate(degree)
    )
}

@Preview
@Composable
private fun Preview() {
    Throbber()
}
