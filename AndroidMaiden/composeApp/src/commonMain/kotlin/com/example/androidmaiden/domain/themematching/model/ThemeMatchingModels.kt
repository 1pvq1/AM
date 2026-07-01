package com.example.androidmaiden.domain.themematching.model

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

/**
 * Represents a specific interactive character animation action.
 */
enum class CharacterAction(val label: String, val icon: ImageVector, val desc: String) {
    IDLE("Idle", Icons.Default.HourglassEmpty, "Slow bobbing & soft breathing effect"),
    WALKING("Walking", Icons.Default.DirectionsWalk, "Rhythmic bounce & swaying rotation"),
    RUNNING("Running", Icons.Default.DirectionsRun, "Forward-tilted fast movement cycle"),
    WAVING("Waving", Icons.Default.Accessibility, "High frequency arm/body wave with emoji bubble"),
    JUMPING("Jumping", Icons.Default.ArrowUpward, "Squash & stretch launch with a mid-air flip"),
    SLEEPING("Sleeping", Icons.Default.NightsStay, "Slowed tilted breath with rising Zzz particles")
}

/**
 * Represents a stateful particle for sleeping animations.
 */
data class SleepParticle(
    val id: Int,
    val x: Float,
    val y: Float,
    val alpha: Float
)

/**
 * Represents a row entry for character stats.
 */
data class CharacterStats(
    val attribute: String,
    val value: String,
    val modifier: String
)

/**
 * Represents a theme color palette role and information.
 */
data class ThemePalette(
    val colorRole: String,
    val color: Color,
    val usageContext: String
)

/**
 * Represents a row entry for animation configurations.
 */
data class AnimationConfig(
    val action: CharacterAction,
    val speedMs: String,
    val isLoop: Boolean,
    val easingStyle: String
)
