package ru.trfx.dailyquiz.ui.component

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.trfx.dailyquiz.ui.theme.Colors
import ru.trfx.dailyquiz.ui.theme.Shapes

@Composable
fun AnswerOption(
    state: RadioButtonState,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = LocalIndication.current,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.Button)
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                enabled = enabled,
                onClick = onClick,
            )
            .let {
                when (state) {
                    RadioButtonState.Off -> it.background(
                        color = Colors.Silver,
                        shape = Shapes.Button
                    )

                    else -> it.border(width = 1.dp, color = state.color, shape = Shapes.Button)
                }
            }
            .padding(16.dp),
    ) {
        RadioButton(state = state, onClick = {}, enabled = false)
        Text(
            text = text,
            fontSize = 14.sp,
            color = state.color,
        )
    }
}

@Preview
@Composable
private fun PreviewAll() {
    val text = "Lorem ipsum"
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        AnswerOption(RadioButtonState.Off, text, onClick = {})
        AnswerOption(RadioButtonState.Selected, text, onClick = {})
        AnswerOption(RadioButtonState.Correct, text, onClick = {})
        AnswerOption(RadioButtonState.Incorrect, text, onClick = {})
    }
}
