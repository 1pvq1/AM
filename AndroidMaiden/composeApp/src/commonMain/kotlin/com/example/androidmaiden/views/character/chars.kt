package com.example.androidmaiden.views.character

import androidmaiden.composeapp.generated.resources.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.androidmaiden.views.PreviewItem
import org.jetbrains.compose.ui.tooling.preview.Preview


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
fun CharacterIllustration() {
    val infiniteTransition = rememberInfiniteTransition(label = "floatAnim")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 12f, // 上下浮动幅度
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
        ), label = "offsetY"
    )

    Image(
        painter = painterResource(Res.drawable.char_androidMaiden_full), // TODO: 换成角色立绘资源
        contentDescription = "角色展示",
        modifier = Modifier.size(200.dp).offset(y = offsetY.dp) // 应用动画偏移
    )
}

@Composable
fun CharacterIllustrationBox() {
    Surface(
        shape = RoundedCornerShape(16.dp), // 圆角边框
        tonalElevation = 4.dp,             // 阴影/浮起效果
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline), // 边框
        color = MaterialTheme.colorScheme.surfaceVariant, // 背景色
        modifier = Modifier
            .size(width = 200.dp, height = 240.dp) // 控制立绘区域大小
            .padding(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CharacterIllustration()
        }
    }
}

@Composable
fun CharacterDialog(dialogText: String, modifier: Modifier = Modifier) {   // 对话框 + 三角形
    // 先取出颜色（在 Composable 上下文里）
    val surfaceColor = MaterialTheme.colorScheme.surface
    val outlineColor = MaterialTheme.colorScheme.outline

    Box(
        modifier = Modifier, contentAlignment = Alignment.CenterStart
    ) {
        // 对话框主体
        Surface(
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 2.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = dialogText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(12.dp)
            )
        }

        // 三角形指向人物
        Canvas(
            modifier = Modifier.size(16.dp).align(Alignment.CenterStart)
        ) {
            val path = Path().apply {
                moveTo(0f, size.height / 2f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                close()
            }
            drawPath(
                path = path, color = surfaceColor, style = Fill
            )
            drawPath(
                path = path, color = outlineColor, style = Stroke(width = 1f)
            )
        }
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
                CharacterIllustrationBox()
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
                CharacterIllustrationBox()
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
                CharacterIllustrationBox()

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

