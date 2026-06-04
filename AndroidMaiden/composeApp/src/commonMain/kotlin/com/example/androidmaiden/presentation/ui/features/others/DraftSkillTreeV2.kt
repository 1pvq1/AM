package com.example.androidmaiden.presentation.ui.features.others

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.domain.model.Screen
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

@Composable
fun DraftSkillTreeV2(
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    // Define the color palette
    val darkBg = Color(0xFF0A0F1D)
    val goldColor = Color(0xFFFFD700)
    val systemColor = Color(0xFF4CAF50)
    val soulLinkColor = Color(0xFF00E5FF)
    val logicColor = Color(0xFFD500F9)

    // Build the nodes list based on app integration
    val nodes = remember {
        listOf(
            // Central Core
            SkillNodeV2(
                id = "core",
                title = "Maiden Core",
                description = "The central processor linking your device and the Maiden's cognitive consciousness. Upgrading this node increases system performance.",
                icon = Icons.Default.Favorite,
                x = 0.5f,
                y = 0.45f,
                color = goldColor,
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
                color = systemColor,
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
                color = systemColor,
                screen = null,
                isUnlocked = false,
                branch = "System"
            ),
            SkillNodeV2(
                id = "cpu",
                title = "Processor Chip",
                description = "Optimize CPU core cycles during intensive computational tasks. (Under Development)",
                icon = Icons.Default.DeveloperBoard,
                x = 0.35f,
                y = 0.18f,
                color = systemColor,
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
                color = systemColor,
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
                color = systemColor,
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
                color = soulLinkColor,
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
                color = soulLinkColor,
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
                color = soulLinkColor,
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
                color = soulLinkColor,
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
                color = soulLinkColor,
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
                color = logicColor,
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
                color = logicColor,
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
                color = logicColor,
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
                color = logicColor,
                screen = null,
                isUnlocked = false,
                branch = "Logic"
            )
        )
    }

    var selectedNode by remember { mutableStateOf<SkillNodeV2?>(null) }

    // Infinite animations for visual premium feedback
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulsingScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsingScale"
    )
    val glowingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowingAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF0F1A30), darkBg),
                    radius = 1200f
                )
            )
    ) {
        // Draw background cyber grid
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

        // Draw connections between nodes
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val coreOffset = Offset(w * 0.5f, h * 0.45f)

            // Draw branch titles
            // Draw lines and connectors
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

        // Render branch labels
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "SYSTEM",
                color = systemColor.copy(alpha = 0.4f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 24.dp, top = 80.dp)
            )
            Text(
                text = "SOUL LINK",
                color = soulLinkColor.copy(alpha = 0.4f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 24.dp, top = 80.dp)
            )
            Text(
                text = "LOGIC CORE",
                color = logicColor.copy(alpha = 0.4f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 200.dp)
            )
        }

        // Render nodes
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val w = maxWidth
            val h = maxHeight

            nodes.forEach { node ->
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
                            .clickable {
                                selectedNode = node
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = node.icon,
                            contentDescription = node.title,
                            tint = if (node.isUnlocked) Color.White else Color(0xFF64748B),
                            modifier = Modifier.size(if (node.id == "core") 28.dp else 22.dp)
                        )

                        // If locked, draw overlay padlock icon badge
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
        }

        // Sleek cybernetic HUD Node detail bottom card
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
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
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
                            IconButton(onClick = { selectedNode = null }) {
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
                                    selectedNode = null
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
                                color = goldColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        } else {
                            // Locked node feedback with unavailable visuals
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
}
