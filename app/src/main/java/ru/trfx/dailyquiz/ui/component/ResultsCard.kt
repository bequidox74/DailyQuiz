package ru.trfx.dailyquiz.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.ui.theme.Colors
import ru.trfx.dailyquiz.ui.theme.Shapes

private const val MAX_STARS = 5

@Composable
fun ResultsCard(
    stars: Int,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color = Colors.White, shape = Shapes.Card)
            .padding(24.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(stars) {
                Image(
                    painter = painterResource(R.drawable.ic_star_active),
                    contentDescription = null,
                )
            }
            repeat(MAX_STARS - stars) {
                Image(
                    painter = painterResource(R.drawable.ic_star_inactive),
                    contentDescription = null,
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.results_correct_questions, stars, MAX_STARS),
            color = Colors.Yellow,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(
                when (stars) {
                    0 -> R.string.results_0_stars_title
                    1 -> R.string.results_1_stars_title
                    2 -> R.string.results_2_stars_title
                    3 -> R.string.results_3_stars_title
                    4 -> R.string.results_4_stars_title
                    5 -> R.string.results_5_stars_title
                    else -> error("Invalid star count")
                }
            ),
            color = Colors.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(
                when (stars) {
                    0 -> R.string.results_0_stars_body
                    1 -> R.string.results_1_stars_body
                    2 -> R.string.results_2_stars_body
                    3 -> R.string.results_3_stars_body
                    4 -> R.string.results_4_stars_body
                    5 -> R.string.results_5_stars_body
                    else -> error("Invalid star count")
                }
            ),
            color = Colors.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(52.dp))
        Button(
            text = stringResource(R.string.button_restart).uppercase(),
            color = Colors.Blue,
            textColor = Colors.White,
            onClick = onRestart,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ResultsCard(
        stars = 4,
        onRestart = {},
    )
}
