package com.example.androidmaiden.views.character

import androidmaiden.composeapp.generated.resources.Res
import androidmaiden.composeapp.generated.resources.char_androidMaiden_full
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.views.PreviewItem
import org.jetbrains.compose.resources.painterResource


@Composable
fun charPreviewItems() = listOf(
    PreviewItem("CharacterIllustrationBox") { CharacterIllustrationBox() },
    PreviewItem("CharacterIllustration") { CharacterIllustration() },
    PreviewItem("Dialog 短文本") { CharacterDialog("Hi") },
    PreviewItem("Dialog 长文本") { CharacterDialog("这是一个比较长的对话框文本") },
    PreviewItem("WithDialog 横向") { CharacterWithDialog("横向布局", layout = CharacterLayout.Horizontal) },
    PreviewItem("WithDialog 纵向") { CharacterWithDialog("纵向布局", layout = CharacterLayout.Vertical) }
)

@Composable
fun CharacterIllustration(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.char_androidMaiden_full), // TODO: 换成角色立绘资源
        contentDescription = "角色展示",
        contentScale = ContentScale.Fit, // This will ensure the image fits and is not cropped
        modifier = modifier
    )
}

@Composable
fun CharacterIllustrationBox(modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp), // 圆角边框
        tonalElevation = 4.dp,             // 阴影/浮起效果
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline), // 边框
        color = MaterialTheme.colorScheme.surfaceVariant, // 背景色
        modifier = modifier // Apply modifier for size here
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().padding(8.dp) // Padding inside the box
        ) {
            AnimatedFloating { animationModifier ->
                CharacterIllustration(
                    // Apply animation after sizing
                    modifier = Modifier.fillMaxSize().then(animationModifier)
                )
            }
        }
    }
}

private class DialogBubbleShape(
    private val cornerRadius: Dp,
    private val arrowWidth: Dp,
    private val arrowHeight: Dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val arrowWidthPx = with(density) { arrowWidth.toPx() }
        val arrowHeightPx = with(density) { arrowHeight.toPx() }
        // 三角形指向人物
        val path = Path().apply {
            val roundRect = RoundRect(
                left = arrowWidthPx,
                top = 0f,
                right = size.width,
                bottom = size.height,
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
            )
            addRoundRect(roundRect)

            moveTo(0f, size.height / 2)
            lineTo(arrowWidthPx, size.height / 2 - arrowHeightPx / 2)
            lineTo(arrowWidthPx, size.height / 2 + arrowHeightPx / 2)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun CharacterDialog(dialogText: String, modifier: Modifier = Modifier) {
    val arrowWidth = 8.dp
    val arrowHeight = 16.dp
    Surface(
        modifier = modifier,
        shape = DialogBubbleShape(cornerRadius = 12.dp, arrowWidth = arrowWidth, arrowHeight = arrowHeight),
        tonalElevation = 2.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        color = MaterialTheme.colorScheme.surface
    ) {
        Text(
            text = dialogText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(
                start = arrowWidth + 12.dp,
                end = 12.dp,
                top = 12.dp,
                bottom = 12.dp
            )
        )
    }
}

enum class CharacterLayout {
    Horizontal, Vertical, Floating
}

@Composable
fun CharacterWithDialog(
    dialogText: String,
    layout: CharacterLayout = CharacterLayout.Horizontal
) {
    when (layout) {
        CharacterLayout.Horizontal -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                CharacterIllustrationBox(modifier = Modifier.size(width = 200.dp, height = 240.dp))
                Spacer(Modifier.width(12.dp))
                CharacterDialog(dialogText)
            }
        }

        CharacterLayout.Vertical -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CharacterIllustrationBox(modifier = Modifier.size(width = 200.dp, height = 240.dp))
                Spacer(Modifier.height(12.dp))
                CharacterDialog(dialogText)
            }
        }

        CharacterLayout.Floating -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                ) {
                CharacterIllustrationBox(modifier = Modifier.size(width = 200.dp, height = 240.dp))

                // 可拖动对话框
                MovableDialog(dialogText)
            }
        }
    }
}

@Composable
fun MovableDialog(dialogText: String) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    CharacterDialog(
        dialogText = dialogText,
        modifier = Modifier
            .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume() // 消费事件，避免冲突
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    )
}
