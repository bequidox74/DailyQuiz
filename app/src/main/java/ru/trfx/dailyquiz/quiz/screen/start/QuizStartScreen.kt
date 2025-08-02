package ru.trfx.dailyquiz.quiz.screen.start

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.quiz.Category
import ru.trfx.dailyquiz.quiz.Difficulty
import ru.trfx.dailyquiz.quiz.screen.BaseQuizScreen
import ru.trfx.dailyquiz.ui.component.Button
import ru.trfx.dailyquiz.ui.component.Card
import ru.trfx.dailyquiz.ui.component.ComboBox
import ru.trfx.dailyquiz.ui.theme.Colors

@Destination<RootGraph>
@Composable
fun QuizStartScreen() {
    BaseQuizScreen(
        title = null,
        showBackButton = false,
        onBackButtonClick = {},
    ) {
        Card {
            var selectedCategory by remember { mutableIntStateOf(-1) }
            var selectedDifficulty by remember { mutableIntStateOf(-1) }

            val resources = LocalContext.current.resources
            val categories =
                remember { Category.entries.map { resources.getString(it.resId) } }
            val difficulties =
                remember { Difficulty.entries.map { resources.getString(it.resId) } }

            Text(
                text = stringResource(R.string.text_almost_ready),
                color = Colors.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.text_filters_description),
                color = Colors.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(48.dp))
            ComboBox(
                title = stringResource(R.string.label_category),
                options = categories,
                selectionIndex = selectedCategory,
                onOptionSelect = { selectedCategory = it },
            )
            Spacer(Modifier.height(16.dp))
            ComboBox(
                title = stringResource(R.string.label_difficulty),
                options = difficulties,
                selectionIndex = selectedDifficulty,
                onOptionSelect = { selectedDifficulty = it },
            )
            Spacer(Modifier.height(48.dp))
            Button(
                text = stringResource(R.string.button_start_quiz).uppercase(),
                onClick = {},
                enabled = selectedCategory >= 0 && selectedDifficulty >= 0,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    QuizStartScreen()
}
