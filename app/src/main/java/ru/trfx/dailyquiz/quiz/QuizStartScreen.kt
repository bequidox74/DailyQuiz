package ru.trfx.dailyquiz.quiz

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.koin.androidx.compose.koinViewModel
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.ui.component.Button
import ru.trfx.dailyquiz.ui.component.Card
import ru.trfx.dailyquiz.ui.component.ComboBox
import ru.trfx.dailyquiz.ui.theme.Colors

@Destination<RootGraph>
@Composable
fun QuizStartScreen(
    viewModel: QuizStartScreenViewModel = koinViewModel(),
) {
    val selectedCategoryIndex by viewModel.selectedCategoryIndex.collectAsStateWithLifecycle()
    val selectedDifficultyIndex by viewModel.selectedDifficultyIndex.collectAsStateWithLifecycle()

    QuizStartScreen(
        categories = viewModel.categories.map { stringResource(it.resId) },
        selectedCategoryIndex = selectedCategoryIndex,
        onCategorySelect = {},
        difficulties = viewModel.categories.map { stringResource(it.resId) },
        selectedDifficultyIndex = selectedDifficultyIndex,
        onDifficultySelect = {},
    )
}

@Composable
fun QuizStartScreen(
    categories: List<String>,
    selectedCategoryIndex: Int,
    onCategorySelect: (Int) -> Unit,
    difficulties: List<String>,
    selectedDifficultyIndex: Int,
    onDifficultySelect: (Int) -> Unit,
) {
    BaseQuizScreen(
        title = null,
        showBackButton = false,
        onBackButtonClick = {},
    ) {
        Card {
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
                selectionIndex = selectedCategoryIndex,
                onOptionSelect = onCategorySelect,
            )
            Spacer(Modifier.height(16.dp))
            ComboBox(
                title = stringResource(R.string.label_difficulty),
                options = difficulties,
                selectionIndex = selectedDifficultyIndex,
                onOptionSelect = onDifficultySelect,
            )
            Spacer(Modifier.height(48.dp))
            Button(
                text = stringResource(R.string.button_start_quiz).uppercase(),
                onClick = {},
                enabled = false,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var category by remember { mutableIntStateOf(-1) }
    var difficulty by remember { mutableIntStateOf(-1) }
    QuizStartScreen(
        categories = listOf("A", "B", "C", "D"),
        selectedCategoryIndex = category,
        onCategorySelect = { category = it },
        difficulties = listOf("X", "Y", "Z"),
        selectedDifficultyIndex = difficulty,
        onDifficultySelect = { difficulty = it },
    )
}
