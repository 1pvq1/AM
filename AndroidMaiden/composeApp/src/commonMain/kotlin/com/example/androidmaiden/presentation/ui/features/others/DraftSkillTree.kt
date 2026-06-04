package com.example.androidmaiden.presentation.ui.features.others

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A draft implementation of an RPG-style Skill Tree for AndroidMaiden.
 * Designed by Nano Banana (AI Assistant).
 */
@Preview
@Composable
fun DraftSkillTreePreview() {
    MaterialTheme {
        DraftSkillTree()
    }
}

@Composable
fun DraftSkillTree(onNodeClick: (SkillNode) -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        // Simple background "grid" or "stars" could be added here
        
        // Render Connections (Lines)
        SkillTreeConnections()

        // Render Nodes
        SkillTreeNodes(onNodeClick)
    }
}

data class SkillNode(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val x: Float, // Normalized 0-1
    val y: Float, // Normalized 0-1
    val color: Color,
    val level: Int = 1
)

@Composable
fun SkillTreeNodes(onNodeClick: (SkillNode) -> Unit) {
    val nodes = listOf(
        // Core
        SkillNode("core", "Maiden Core", Icons.Default.Favorite, 0.5f, 0.5f, Color(0xFFFFD700)),
        
        // System Branch
        SkillNode("files", "File Mgmt", Icons.Default.Folder, 0.3f, 0.3f, Color(0xFF4CAF50)),
        SkillNode("cleanup", "Auto Clean", Icons.Default.AutoFixHigh, 0.2f, 0.15f, Color(0xFF8BC34A), 2),
        SkillNode("hw", "Hardware", Icons.Default.Memory, 0.7f, 0.3f, Color(0xFF4CAF50)),
        
        // Interaction Branch
        SkillNode("chat", "Interaction", Icons.Default.Forum, 0.3f, 0.7f, Color(0xFF2196F3)),
        SkillNode("memory", "Memory", Icons.Default.Psychology, 0.2f, 0.85f, Color(0xFF03A9F4), 2),
        
        // Utility Branch
        SkillNode("tasks", "Tasks", Icons.Default.Checklist, 0.7f, 0.7f, Color(0xFF9C27B0)),
        SkillNode("theme", "Theming", Icons.Default.Palette, 0.85f, 0.8f, Color(0xFFE91E63), 2)
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = maxWidth
        val height = maxHeight

        nodes.forEach { node ->
            SkillNodeItem(
                node = node,
                modifier = Modifier
                    .offset(
                        x = width * node.x - 32.dp,
                        y = height * node.y - 32.dp
                    ),
                onClick = { onNodeClick(node) }
            )
        }
    }
}

@Composable
fun SkillNodeItem(node: SkillNode, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(64.dp)
                .clickable { onClick() },
            shape = CircleShape,
            color = node.color,
            tonalElevation = 8.dp,
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = node.icon,
                    contentDescription = node.title,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Text(
            text = node.title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun SkillTreeConnections() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        val center = Offset(width * 0.5f, height * 0.5f)
        
        // Helper to draw connection from center to normalized x, y
        fun drawLineTo(x: Float, y: Float, color: Color) {
            drawLine(
                color = color.copy(alpha = 0.5f),
                start = center,
                end = Offset(width * x, height * y),
                strokeWidth = 4f
            )
        }

        // Connections to Branch Roots
        drawLineTo(0.3f, 0.3f, Color(0xFF4CAF50)) // To File Mgmt
        drawLineTo(0.7f, 0.3f, Color(0xFF4CAF50)) // To Hardware
        drawLineTo(0.3f, 0.7f, Color(0xFF2196F3)) // To Interaction
        drawLineTo(0.7f, 0.7f, Color(0xFF9C27B0)) // To Tasks
        
        // Internal Branch Connections
        drawLine(
            color = Color(0xFF4CAF50).copy(alpha = 0.3f),
            start = Offset(width * 0.3f, height * 0.3f),
            end = Offset(width * 0.2f, height * 0.15f),
            strokeWidth = 2f
        )
        drawLine(
            color = Color(0xFF2196F3).copy(alpha = 0.3f),
            start = Offset(width * 0.3f, height * 0.7f),
            end = Offset(width * 0.2f, height * 0.85f),
            strokeWidth = 2f
        )
        drawLine(
            color = Color(0xFF9C27B0).copy(alpha = 0.3f),
            start = Offset(width * 0.7f, height * 0.7f),
            end = Offset(width * 0.85f, height * 0.8f),
            strokeWidth = 2f
        )
    }
}
