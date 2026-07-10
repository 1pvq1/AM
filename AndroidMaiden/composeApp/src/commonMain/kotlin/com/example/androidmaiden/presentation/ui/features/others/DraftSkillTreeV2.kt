package com.example.androidmaiden.presentation.ui.features.others

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.domain.model.Screen
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Node data structure for V2 RPG Skill Tree.
 */
data class SkillNodeV2(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val x: Float, // Normalized x (0f to 1f)
    val y: Float, // Normalized y (0f to 1f)
    val color: Color,
    val screen: Screen?, // Target screen if unlocked and active
    val isUnlocked: Boolean,
    val branch: String // "System", "Soul Link", "Logic", or "Core"
)

private object SkillTreeColors {
    val DarkBg = Color(0xFF0A0F1D)
    val Gold = Color(0xFFFFD700)
    val System = Color(0xFF4CAF50)
    val SoulLink = Color(0xFF00E5FF)
    val Logic = Color(0xFFD500F9)
}

/**
 * State holder for the Skill Tree to improve maintainability and testability.
 */
@Stable
class SkillTreeState(
    initialZoom: Float = 1f,
    initialPan: Offset = Offset.Zero
) {
    var zoomScale by mutableStateOf(initialZoom)
    var panOffset by mutableStateOf(initialPan)
    var selectedNode by mutableStateOf<SkillNodeV2?>(null)

    fun onZoomIn() {
        zoomScale = (zoomScale * 1.2f).coerceIn(0.5f, 3f)
    }

    fun onZoomOut() {
        zoomScale = (zoomScale / 1.2f).coerceIn(0.5f, 3f)
    }

    fun reset() {
        zoomScale = 1f
        panOffset = Offset.Zero
    }

    fun updateTransform(zoomChange: Float, offsetChange: Offset) {
        zoomScale = (zoomScale * zoomChange).coerceIn(0.5f, 3f)
        panOffset += offsetChange
    }
}

@Composable
fun rememberSkillTreeState(): SkillTreeState {
    return remember { SkillTreeState() }
}

@Composable
fun DraftSkillTreeV2(
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val nodes = rememberSkillTreeNodes()
    val state = rememberSkillTreeState()

    val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
        state.updateTransform(zoomChange, offsetChange)
    }

    val pulsingScale = rememberPulsingScale()
    val glowingAlpha = rememberGlowingAlpha()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF0F1A30), SkillTreeColors.DarkBg),
                    radius = 1200f
                )
            )
            .transformable(state = transformState)
    ) {
        // Zoomable and Pannable Content Layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = state.zoomScale,
                    scaleY = state.zoomScale,
                    translationX = state.panOffset.x,
                    translationY = state.panOffset.y
                )
        ) {
            SkillTreeGrid()

            SkillTreeConnections(nodes)

            SkillTreeBranchLabels()

            SkillTreeNodes(
                nodes = nodes,
                pulsingScale = pulsingScale,
                glowingAlpha = glowingAlpha,
                onNodeClick = { state.selectedNode = it }
            )
        }

        SkillTreeZoomControls(
            zoomScale = state.zoomScale,
            onZoomIn = state::onZoomIn,
            onZoomOut = state::onZoomOut,
            onReset = state::reset,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .padding(bottom = 80.dp)
        )

        SkillNodeDetailCard(
            selectedNode = state.selectedNode,
            onDismiss = { state.selectedNode = null },
            onNavigate = onNavigate,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
private fun rememberSkillTreeNodes(): List<SkillNodeV2> {
    return remember {
        listOf(
            // Central Core
            SkillNodeV2(
                id = "core",
                title = "Maiden Core",
                description = "The central processor linking your device and the Maiden's cognitive consciousness. Upgrading this node increases system performance.",
                icon = Icons.Default.Favorite,
                x = 0.5f,
                y = 0.45f,
                color = SkillTreeColors.Gold,
                screen = null,
                isUnlocked = true,
                branch = "Core"
            ),

            // SYSTEM BRANCH (Left - Green)
            SkillNodeV2(
                id = "files",
                title = "File Management",
                description = "Unlocks native file access. Analyze disk usage, categorize audio, images, videos, and optimize device memory capacity.",
                icon = Icons.Default.Folder,
                x = 0.28f,
                y = 0.32f,
                color = SkillTreeColors.System,
                screen = Screen.Files,
                isUnlocked = true,
                branch = "System"
            ),
            SkillNodeV2(
                id = "hardware",
                title = "Hardware Monitor",
                description = "Monitor system resources in real-time, tracking battery temperature, CPU core allocation, and RAM states.",
                icon = Icons.Default.Hardware,
                x = 0.20f,
                y = 0.20f,
                color = SkillTreeColors.System,
                screen = Screen.Hardware,
                isUnlocked = true,
                branch = "System"
            ),
            SkillNodeV2(
                id = "cpu",
                title = "Processor Chip",
                description = "Optimize CPU core cycles during intensive computational tasks. (Under Development)",
                icon = Icons.Default.DeveloperBoard,
                x = 0.35f,
                y = 0.18f,
                color = SkillTreeColors.System,
                screen = null,
                isUnlocked = false,
                branch = "System"
            ),
            SkillNodeV2(
                id = "network",
                title = "Network Diagnostics",
                description = "Scans connection bandwidth, analyzes package drops, and optimizes API request latency. (Under Development)",
                icon = Icons.Default.Wifi,
                x = 0.16f,
                y = 0.38f,
                color = SkillTreeColors.System,
                screen = null,
                isUnlocked = false,
                branch = "System"
            ),
            SkillNodeV2(
                id = "clean",
                title = "Auto Cleanup",
                description = "Intelligent automated cleaner that sweeps transient caches and manages background processes. (Under Development)",
                icon = Icons.Default.AutoFixHigh,
                x = 0.16f,
                y = 0.52f,
                color = SkillTreeColors.System,
                screen = null,
                isUnlocked = false,
                branch = "System"
            ),

            // SOUL LINK BRANCH (Right - Blue/Cyan)
            SkillNodeV2(
                id = "chat",
                title = "Character Chat",
                description = "Establishes a high-level cognitive dialogue link to chat directly with your AI Maiden companion.",
                icon = Icons.Default.Forum,
                x = 0.72f,
                y = 0.32f,
                color = SkillTreeColors.SoulLink,
                screen = Screen.CharacterInteraction,
                isUnlocked = true,
                branch = "Soul Link"
            ),
            SkillNodeV2(
                id = "empathy",
                title = "Empathy Psyche",
                description = "Enables sentimental tracking and logs emotion profiles to build conversational rapport. (Under Development)",
                icon = Icons.Default.Psychology,
                x = 0.80f,
                y = 0.20f,
                color = SkillTreeColors.SoulLink,
                screen = null,
                isUnlocked = false,
                branch = "Soul Link"
            ),
            SkillNodeV2(
                id = "connection",
                title = "Device Bond",
                description = "Links your local system states with conversation triggers to enable custom context queries. (Under Development)",
                icon = Icons.Default.Link,
                x = 0.84f,
                y = 0.32f,
                color = SkillTreeColors.SoulLink,
                screen = null,
                isUnlocked = false,
                branch = "Soul Link"
            ),
            SkillNodeV2(
                id = "bonding",
                title = "Soul Bonding",
                description = "Unlock customized cosmetic themes, premium character dialogues, and companion interaction levels. (Under Development)",
                icon = Icons.Default.Diversity3,
                x = 0.84f,
                y = 0.45f,
                color = SkillTreeColors.SoulLink,
                screen = null,
                isUnlocked = false,
                branch = "Soul Link"
            ),
            SkillNodeV2(
                id = "sync",
                title = "Memory Sync",
                description = "Backs up conversation parameters, personality tweaks, and dialogue logs securely to the cloud. (Under Development)",
                icon = Icons.Default.CloudSync,
                x = 0.80f,
                y = 0.55f,
                color = SkillTreeColors.SoulLink,
                screen = null,
                isUnlocked = false,
                branch = "Soul Link"
            ),

            // LOGIC BRANCH (Bottom - Purple)
            SkillNodeV2(
                id = "tasks",
                title = "Task Management",
                description = "Unlocks checklist capabilities. Organize lists, structure agendas, and set active timers with productivity assistance.",
                icon = Icons.Default.Checklist,
                x = 0.50f,
                y = 0.68f,
                color = SkillTreeColors.Logic,
                screen = Screen.Todo,
                isUnlocked = true,
                branch = "Logic"
            ),
            SkillNodeV2(
                id = "creativity",
                title = "Theme Engine",
                description = "Dynamically adjusts system themes. Set Material You accent colors, dark modes, and high-tech typography. (Under Development)",
                icon = Icons.Default.Palette,
                x = 0.35f,
                y = 0.76f,
                color = SkillTreeColors.Logic,
                screen = null,
                isUnlocked = false,
                branch = "Logic"
            ),
            SkillNodeV2(
                id = "analysis",
                title = "Statistics Logs",
                description = "Analyze daily analytics, diagnostic logs, and view performance charts tracking Maiden activity. (Under Development)",
                icon = Icons.Default.BarChart,
                x = 0.65f,
                y = 0.76f,
                color = SkillTreeColors.Logic,
                screen = null,
                isUnlocked = false,
                branch = "Logic"
            ),
            SkillNodeV2(
                id = "mechanics",
                title = "Core Mechanics",
                description = "Configure custom logical loops, automation rules, and trigger conditions. (Under Development)",
                icon = Icons.Default.Settings,
                x = 0.50f,
                y = 0.84f,
                color = SkillTreeColors.Logic,
                screen = null,
                isUnlocked = false,
                branch = "Logic"
            )
        )
    }
}

@Composable
private fun SkillTreeGrid() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val gridSpacing = 60.dp.toPx()
        val w = size.width
        val h = size.height

        // Vertical grid lines
        var x = 0f
        while (x < w) {
            drawLine(
                color = Color(0xFF1E293B).copy(alpha = 0.25f),
                start = Offset(x, 0f),
                end = Offset(x, h),
                strokeWidth = 1f
            )
            x += gridSpacing
        }

        // Horizontal grid lines
        var y = 0f
        while (y < h) {
            drawLine(
                color = Color(0xFF1E293B).copy(alpha = 0.25f),
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = 1f
            )
            y += gridSpacing
        }
    }
}

@Composable
private fun SkillTreeConnections(nodes: List<SkillNodeV2>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        
        // Find core node dynamically for extensibility
        val coreNode = nodes.find { it.id == "core" }
        val coreOffset = coreNode?.let { Offset(w * it.x, h * it.y) } ?: Offset(w * 0.5f, h * 0.45f)

        nodes.forEach { node ->
            if (node.id != "core") {
                val endOffset = Offset(w * node.x, h * node.y)
                val isPathUnlocked = node.isUnlocked

                if (isPathUnlocked) {
                    // Glowing solid line
                    drawLine(
                        color = node.color.copy(alpha = 0.2f),
                        start = coreOffset,
                        end = endOffset,
                        strokeWidth = 8f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = node.color,
                        start = coreOffset,
                        end = endOffset,
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )
                } else {
                    // Locked faded dashed line
                    drawLine(
                        color = Color(0xFF475569).copy(alpha = 0.35f),
                        start = coreOffset,
                        end = endOffset,
                        strokeWidth = 2f,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SkillTreeBranchLabels() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "SYSTEM",
            color = SkillTreeColors.System.copy(alpha = 0.4f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 80.dp)
        )
        Text(
            text = "SOUL LINK",
            color = SkillTreeColors.SoulLink.copy(alpha = 0.4f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 24.dp, top = 80.dp)
        )
        Text(
            text = "LOGIC CORE",
            color = SkillTreeColors.Logic.copy(alpha = 0.4f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 200.dp)
        )
    }
}

@Composable
private fun SkillTreeNodes(
    nodes: List<SkillNodeV2>,
    pulsingScale: Float,
    glowingAlpha: Float,
    onNodeClick: (SkillNodeV2) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val w = maxWidth
        val h = maxHeight

        nodes.forEach { node ->
            SkillNodeItem(
                node = node,
                w = w,
                h = h,
                pulsingScale = pulsingScale,
                glowingAlpha = glowingAlpha,
                onClick = { onNodeClick(node) }
            )
        }
    }
}

@Composable
private fun SkillNodeItem(
    node: SkillNodeV2,
    w: androidx.compose.ui.unit.Dp,
    h: androidx.compose.ui.unit.Dp,
    pulsingScale: Float,
    glowingAlpha: Float,
    onClick: () -> Unit
) {
    val nodeScale = if (node.id == "core") pulsingScale * 1.1f else if (node.isUnlocked) pulsingScale else 1f
    val borderGlow = if (node.isUnlocked) node.color.copy(alpha = glowingAlpha) else Color.Transparent

    Column(
        modifier = Modifier
            .offset(
                x = w * node.x - 36.dp,
                y = h * node.y - 36.dp
            )
            .scale(nodeScale)
            .width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(if (node.id == "core") 64.dp else 52.dp)
                .clip(if (node.id == "core") RoundedCornerShape(16.dp) else CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            if (node.isUnlocked) node.color.copy(alpha = 0.25f) else Color(0xFF1E293B),
                            Color(0xFF0F172A)
                        )
                    )
                )
                .border(
                    width = if (node.isUnlocked) 2.dp else 1.dp,
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            if (node.isUnlocked) node.color else Color(0xFF475569),
                            borderGlow,
                            if (node.isUnlocked) node.color else Color(0xFF475569)
                        )
                    ),
                    shape = if (node.id == "core") RoundedCornerShape(16.dp) else CircleShape
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = node.icon,
                contentDescription = node.title,
                tint = if (node.isUnlocked) Color.White else Color(0xFF64748B),
                modifier = Modifier.size(if (node.id == "core") 28.dp else 22.dp)
            )

            if (!node.isUnlocked) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier
                        .size(14.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-2).dp, y = 2.dp)
                )
            }
        }
        Text(
            text = node.title,
            color = if (node.isUnlocked) Color.White else Color(0xFF94A3B8),
            fontSize = 9.sp,
            fontWeight = if (node.isUnlocked) FontWeight.Bold else FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun SkillTreeZoomControls(
    zoomScale: Float,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FloatingActionButton(
            onClick = onZoomIn,
            containerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
            contentColor = Color.White,
            modifier = Modifier.size(40.dp),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Zoom In")
        }
        
        Text(
            text = "${(zoomScale * 100).toInt()}%",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )

        FloatingActionButton(
            onClick = onZoomOut,
            containerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
            contentColor = Color.White,
            modifier = Modifier.size(40.dp),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
        }
        FloatingActionButton(
            onClick = onReset,
            containerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
            contentColor = Color.White,
            modifier = Modifier.size(40.dp),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Reset Zoom")
        }
    }
}

@Composable
private fun SkillNodeDetailCard(
    selectedNode: SkillNodeV2?,
    onDismiss: () -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = selectedNode != null,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(300, easing = EaseOutCubic)
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(200, easing = EaseInCubic)
        ) + fadeOut(),
        modifier = modifier
    ) {
        selectedNode?.let { node ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, node.color.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1527).copy(alpha = 0.95f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(node.color.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = node.icon,
                                    contentDescription = null,
                                    tint = node.color,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = node.title,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Branch: ${node.branch}",
                                    color = node.color.copy(alpha = 0.8f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Detail Panel",
                                tint = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = node.description,
                        color = Color(0xFFCBD5E1),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (node.isUnlocked && node.screen != null) {
                        Button(
                            onClick = {
                                onDismiss()
                                onNavigate(node.screen)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = node.color),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ACCESS CAPABILITY",
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = Color.White
                                )
                            }
                        }
                    } else if (node.id == "core") {
                        Text(
                            text = "CORE ACTIVE & STABLE",
                            color = SkillTreeColors.Gold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .background(Color(0xFF1E293B).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF475569), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "CAPABILITY LOCKED (UNDER DEVELOPMENT)",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberPulsingScale(): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    return infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsingScale"
    ).value
}

@Composable
private fun rememberGlowingAlpha(): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    return infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowingAlpha"
    ).value
}

@Preview
@Composable
fun DraftSkillTreeV2Preview() {
    AppTheme {
        DraftSkillTreeV2(onNavigate = {})
    }
}
