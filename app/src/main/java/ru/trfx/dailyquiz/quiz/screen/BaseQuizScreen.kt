package ru.trfx.dailyquiz.quiz.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.ui.theme.Colors
import ru.trfx.dailyquiz.ui.theme.Shapes

@Composable
inline fun BaseQuizScreen(
    title: String?,
    showBackButton: Boolean,
    noinline onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 96.dp)
            .padding(horizontal = 24.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (showBackButton) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .clip(Shapes.Round)
                        .clickable(
                            onClick = onBackButtonClick,
                            interactionSource = null,
                            indication = null,
                        )
                        .size(48.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = stringResource(R.string.button_back),
                        tint = Color.Unspecified,
                    )
                }
            }

            if (title == null) {
                Image(
                    painter = painterResource(R.drawable.img_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 64.dp)
                        .align(Alignment.Center),
                )
            } else {
                Text(
                    text = title,
                    color = Colors.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }

        Spacer(Modifier.height(42.dp))
        Column {
            content()
        }
    }
}

@Preview
@Composable
private fun PreviewNoTitle() {
    BaseQuizScreen(
        title = null,
        showBackButton = true,
        onBackButtonClick = {},
    ) {
        Spacer(
            Modifier
                .fillMaxWidth()
                .background(Color.White, Shapes.Card)
                .height(200.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewTitle() {
    BaseQuizScreen(
        title = "Lorem ipsum",
        showBackButton = true,
        onBackButtonClick = {},
    ) {
        Spacer(
            Modifier
                .fillMaxWidth()
                .background(Color.White, Shapes.Card)
                .height(200.dp)
        )
    }
}
