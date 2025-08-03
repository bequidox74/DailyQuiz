package ru.trfx.dailyquiz.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.trfx.dailyquiz.R
import ru.trfx.dailyquiz.ui.theme.Colors
import ru.trfx.dailyquiz.ui.theme.Shapes

@Composable
fun ComboBox(
    title: String,
    options: List<String>,
    selectionIndex: Int,
    onOptionSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val hasSelection = selectionIndex >= 0
    val arrowScaleY by animateFloatAsState(
        if (expanded) -1f else 1f,
        label = "arrow",
    )
    val topPadding by animateDpAsState(
        if (hasSelection) 8.dp else 0.dp,
        label = "padding"
    )

    Box(modifier = Modifier.padding(top = topPadding)) {
        Column(
            modifier = modifier
                .clip(Shapes.Button)
                .background(Colors.Silver, Shapes.Button),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .height(48.dp),
            ) {
                Crossfade(
                    targetState = hasSelection,
                    modifier = Modifier.weight(1f)
                ) { state ->
                    if (state) {
                        Text(
                            text = options[selectionIndex],
                            color = Colors.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 12.dp),
                        )
                    } else {
                        Text(
                            text = title,
                            color = Colors.Purple,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 12.dp),
                        )
                    }
                }

                Image(
                    painter = painterResource(R.drawable.ic_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .scale(scaleX = 1f, scaleY = arrowScaleY)
                        .padding(end = 12.dp),
                )
            }

            AnimatedVisibility(expanded) {
                LazyColumn(
                    modifier = Modifier
                        .animateContentSize()
                        .requiredHeightIn(max = 192.dp),
                ) {
                    items(count = options.size) {
                        val option = options[it]
                        Text(
                            text = option,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clickable {
                                    onOptionSelect(it)
                                    expanded = false
                                }
                                .fillMaxWidth()
                                .height(42.dp)
                                .wrapContentSize(Alignment.CenterStart)
                                .padding(start = 12.dp),
                        )
                    }
                }
            }
        }

        AnimatedVisibility(hasSelection) {
            Text(
                text = title,
                color = Colors.Purple,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .offset(y = (-8).dp)
            )
        }
    }

}

@Preview
@Composable
private fun Preview() {
    var index by remember { mutableIntStateOf(-1) }
    ComboBox(
        title = "Category",
        options = listOf("A", "B", "C", "D"),
        selectionIndex = index,
        onOptionSelect = { index = it },
    )
}
