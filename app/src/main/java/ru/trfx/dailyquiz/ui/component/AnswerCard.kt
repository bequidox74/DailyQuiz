package ru.trfx.dailyquiz.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun AnswerCardOptions(
    title: String,
    options: List<AnswerOptionState>,
    onOptionClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            for (index in options.indices) {
                val state = options[index]
                AnswerOption(
                    state = state,
                    enabled = enabled,
                    onClick = { onOptionClick(index) }
                )
            }
        }
    }
}

@Composable
fun HistoryAnswerCard(
    questionNumber: Int,
    questionCount: Int,
    title: String,
    state: RadioButtonState,
    options: List<AnswerOptionState>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(color = Colors.White, shape = Shapes.Card)
            .padding(24.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.question_number, questionNumber, questionCount),
                color = Colors.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.weight(1.0f))
            Icon(
                painter = painterResource(state.iconId),
                contentDescription = stringResource(state.textId),
                tint = Color.Unspecified,
                modifier = Modifier.height(IntrinsicSize.Max),
            )
        }
        Spacer(Modifier.height(24.dp))
        AnswerCardOptions(
            title = title,
            options = options,
            onOptionClick = {},
            enabled = false,
        )
    }
}

@Composable
fun QuestionCard(
    elapsedSeconds: Float,
    totalSeconds: Float,
    currentQuestionNumber: Int,
    questionCount: Int,
    title: String,
    options: List<AnswerOptionState>,
    onOptionClick: (Int) -> Unit,
    optionsEnabled: Boolean,
    buttonText: String,
    buttonEnabled: Boolean,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        QuestionTimer(
            elapsedSeconds = elapsedSeconds,
            totalSeconds = totalSeconds,
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.question_number, currentQuestionNumber, questionCount),
            color = Colors.Lavender,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(24.dp))
        AnswerCardOptions(
            title = title,
            options = options,
            onOptionClick = onOptionClick,
            enabled = optionsEnabled,
        )
        Spacer(Modifier.height(64.dp))
        Button(
            text = buttonText,
            onClick = onButtonClick,
            enabled = buttonEnabled,
        )
    }
}

private const val previewQuestion = "Lorem ipsum dolor sit amet?"
private val previewOptions = listOf(
    AnswerOptionState("Lorem ipsum #1", RadioButtonState.Off),
    AnswerOptionState("Lorem ipsum #2", RadioButtonState.Correct),
    AnswerOptionState("Lorem ipsum #3", RadioButtonState.Incorrect),
    AnswerOptionState("Lorem ipsum #4", RadioButtonState.Selected),
)

@Preview
@Composable
private fun PreviewOptions() {
    AnswerCardOptions(
        title = previewQuestion,
        options = previewOptions,
        onOptionClick = {},
    )
}

@Preview
@Composable
private fun PreviewHistory() {
    HistoryAnswerCard(
        questionNumber = 3,
        questionCount = 5,
        title = previewQuestion,
        state = RadioButtonState.Correct,
        options = previewOptions,
    )
}

@Preview
@Composable
private fun PreviewQuestion() {
    MaterialTheme {
        QuestionCard(
            elapsedSeconds = 150f,
            totalSeconds = 300f,
            currentQuestionNumber = 3,
            questionCount = 5,
            title = previewQuestion,
            options = previewOptions,
            onOptionClick = {},
            buttonText = "Lorem ipsum",
            buttonEnabled = true,
            onButtonClick = {},
            optionsEnabled = true,
        )
    }
}
