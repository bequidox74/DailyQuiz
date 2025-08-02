package ru.trfx.dailyquiz.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.ui.theme.Colors

enum class RadioButtonState(
    @DrawableRes val iconId: Int,
    @StringRes val textId: Int,
    val color: Color,
) {
    Off(R.drawable.ic_radio_off, R.string.radio_off, Colors.Black),
    Selected(R.drawable.ic_radio_selected, R.string.radio_selected, Colors.Purple),
    Correct(R.drawable.ic_radio_correct, R.string.radio_correct, Colors.Green),
    Incorrect(R.drawable.ic_radio_incorrect, R.string.radio_incorrect, Colors.Red),
}

@Composable
fun RadioButton(
    state: RadioButtonState,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(state.iconId),
        contentDescription = stringResource(state.textId),
        tint = Color.Unspecified,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun PreviewAll() {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        RadioButton(RadioButtonState.Off)
        RadioButton(RadioButtonState.Selected)
        RadioButton(RadioButtonState.Correct)
        RadioButton(RadioButtonState.Incorrect)
    }
}
