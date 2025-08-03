package ru.trfx.dailyquiz.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.ui.theme.Colors
import ru.trfx.dailyquiz.ui.theme.Shapes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val STAR_SIZE = 18.dp

@Composable
fun QuizHistoryCard(
    title: String,
    stars: Int,
    dateTime: LocalDateTime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    category: String? = null,
    difficulty: String? = null,
) {
    require(stars in 0..5) { "Invalid number of stars" }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("d MMMM") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(Shapes.Card)
            .clickable(onClick = onClick)
            .background(color = Colors.White, shape = Shapes.Card)
            .padding(24.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = title,
                color = Colors.Purple,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Row {
                repeat(stars) {
                    Image(
                        painter = painterResource(R.drawable.ic_star_active),
                        contentDescription = null,
                        modifier = Modifier.size(STAR_SIZE),
                    )
                }
                repeat(MAX_STARS - stars) {
                    Image(
                        painter = painterResource(R.drawable.ic_star_inactive),
                        contentDescription = null,
                        modifier = Modifier.size(STAR_SIZE),
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = dateFormatter.format(dateTime),
                color = Colors.Black,
                fontSize = 12.sp,
            )
            Text(
                text = timeFormatter.format(dateTime),
                color = Colors.Black,
                fontSize = 12.sp,
            )
        }

        if (category != null || difficulty != null) {
            Spacer(Modifier.height(8.dp))
            if (category != null) {
                Text(
                    text = stringResource(R.string.quiz_category, category),
                    color = Colors.Black,
                    fontSize = 12.sp,
                )
            }

            if (difficulty != null) {
                Text(
                    text = stringResource(R.string.quiz_difficulty, difficulty),
                    color = Colors.Black,
                    fontSize = 12.sp,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    QuizHistoryCard(
        title = "Lorem 1",
        stars = 3,
        dateTime = LocalDateTime.of(2025, 8, 1, 16, 30, 25),
        category = "English Language",
        difficulty = "Hard",
        onClick = {},
    )
}
