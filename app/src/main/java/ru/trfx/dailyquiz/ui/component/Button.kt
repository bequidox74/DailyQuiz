package ru.trfx.dailyquiz.ui.component

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.trfx.dailyquiz.ui.theme.Colors
import ru.trfx.dailyquiz.ui.theme.Shapes

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    invertColors: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = LocalIndication.current,
) {
    val bodyColor = if (!enabled) Colors.Gray else {
        if (invertColors) Colors.White else Colors.Blue
    }
    val textColor = if (!enabled) Colors.White else {
        if (invertColors) Colors.Blue else Colors.White
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(shape = Shapes.Button)
            .clickable(
                onClick = onClick,
                enabled = enabled,
                interactionSource = interactionSource,
                indication = indication,
            )
            .height(50.dp)
            .fillMaxWidth()
            .background(color = bodyColor, shape = Shapes.Button),
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Button(
        text = "LOREM IPSUM",
        onClick = {},
    )
}
