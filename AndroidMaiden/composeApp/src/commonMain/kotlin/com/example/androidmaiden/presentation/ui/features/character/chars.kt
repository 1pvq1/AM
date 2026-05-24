package com.example.androidmaiden.presentation.ui.features.character

import androidmaiden.composeapp.generated.resources.Res
import androidmaiden.composeapp.generated.resources.am_bit
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.example.androidmaiden.presentation.ui.features.PreviewItem
import com.example.androidmaiden.presentation.ui.theme.core.LocalAppExtraShapes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Preview items for the character components.
 * Translated to English for professional standards.
 */
@Composable
fun charPreviewItems() = listOf(
    PreviewItem("CharacterIllustrationBox") { CharacterIllustrationBox() },
    PreviewItem("CharacterIllustration") { CharacterIllustration() },
    PreviewItem("Dialog Short") { CharacterDialog("Hi") },
    PreviewItem("Dialog Long") { CharacterDialog("This is a much longer dialog text for testing layout.") },
    PreviewItem("WithDialog Horizontal") {
        CharacterWithDialog(
            "Horizontal Layout",
            layout = CharacterLayout.Horizontal
        )
    },
    PreviewItem("WithDialog Vertical") {
        CharacterWithDialog(
            "Vertical Layout",
            layout = CharacterLayout.Vertical
        )
    }
)

@Preview
@Composable
fun CharacterIllustration(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.am_bit),
        contentDescription = "Character illustration",
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}

@Preview
@Composable
fun CharacterIllustrationBox(modifier: Modifier = Modifier) {
    Surface(
        shape = LocalAppExtraShapes.current.characterBox,
        tonalElevation = 4.dp,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {
            AnimatedFloating { animationModifier ->
                CharacterIllustration(
                    modifier = Modifier.fillMaxSize().then(animationModifier)
                )
            }
        }
    }
}

/**
 * A custom shape for dialog bubbles that supports RTL (Right-to-Left) layout directions.
 * The arrow always points towards the character (assuming character is at the start).
 */
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
        val isLtr = layoutDirection == LayoutDirection.Ltr

        val path = Path().apply {
            val left = if (isLtr) arrowWidthPx else 0f
            val right = if (isLtr) size.width else size.width - arrowWidthPx

            val roundRect = RoundRect(
                left = left,
                top = 0f,
                right = right,
                bottom = size.height,
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
            )
            addRoundRect(roundRect)

            // Arrow points towards the character side
            if (isLtr) {
                moveTo(0f, size.height / 2)
                lineTo(arrowWidthPx, size.height / 2 - arrowHeightPx / 2)
                lineTo(arrowWidthPx, size.height / 2 + arrowHeightPx / 2)
            } else {
                moveTo(size.width, size.height / 2)
                lineTo(size.width - arrowWidthPx, size.height / 2 - arrowHeightPx / 2)
                lineTo(size.width - arrowWidthPx, size.height / 2 + arrowHeightPx / 2)
            }
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun CharacterDialog(
    dialogText: String,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    arrowWidth: Dp = 8.dp,
    arrowHeight: Dp = 16.dp
) {
    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
    Surface(
        modifier = modifier,
        shape = DialogBubbleShape(cornerRadius, arrowWidth, arrowHeight),
        tonalElevation = 2.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        color = MaterialTheme.colorScheme.surface
    ) {
        Text(
            text = dialogText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(
                start = if (isLtr) arrowWidth + 12.dp else 12.dp,
                end = if (isLtr) 12.dp else arrowWidth + 12.dp,
                top = 12.dp,
                bottom = 12.dp
            )
        )
    }
}

@Preview
@Composable
fun DialogPreview() {
    Column {
        CharacterDialog("Hi")
        CharacterDialog("This is a much longer dialog text for testing layout.")
    }
}


enum class CharacterLayout {
    Horizontal, Vertical, Floating
}

@Composable
fun CharacterWithDialog(
    dialogText: String,
    modifier: Modifier = Modifier,
    layout: CharacterLayout = CharacterLayout.Horizontal
) {
    Box(modifier = modifier.padding(16.dp)) {
        when (layout) {
            CharacterLayout.Horizontal -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    CharacterIllustrationBox(
                        modifier = Modifier.size(
                            width = 200.dp,
                            height = 240.dp
                        )
                    )
                    Spacer(Modifier.width(12.dp))
                    CharacterDialog(dialogText, modifier = Modifier.weight(1f))
                }
            }

            CharacterLayout.Vertical -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CharacterIllustrationBox(
                        modifier = Modifier.size(
                            width = 200.dp,
                            height = 240.dp
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    CharacterDialog(dialogText)
                }
            }

            CharacterLayout.Floating -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    CharacterIllustrationBox(
                        modifier = Modifier.size(
                            width = 200.dp,
                            height = 240.dp
                        )
                    )
                    MovableDialog(dialogText)
                }
            }
        }
    }
}

@Preview
@Composable
fun CharacterPreview() {

        CharacterWithDialog("horizontical", layout = CharacterLayout.Horizontal)
//        CharacterWithDialog("vertical", layout = CharacterLayout.Vertical)
//        CharacterWithDialog("Floating", layout = CharacterLayout.Floating) // bugs

}

/**
 * A draggable dialog box. State is hoisted for better testability and persistence.
 * @param initialOffset Initial position relative to its default placement.
 */
@Composable
fun MovableDialog(
    dialogText: String,
    modifier: Modifier = Modifier,
    initialOffset: IntOffset = IntOffset.Zero,
    onOffsetChange: (IntOffset) -> Unit = {}
) {
    var offset by remember { mutableStateOf(initialOffset) }

    CharacterDialog(
        dialogText = dialogText,
        modifier = modifier
            .offset { offset }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val newOffset = IntOffset(
                        x = offset.x + dragAmount.x.toInt(),
                        y = offset.y + dragAmount.y.toInt()
                    )
                    offset = newOffset
                    onOffsetChange(newOffset)
                }
            }
    )
}
