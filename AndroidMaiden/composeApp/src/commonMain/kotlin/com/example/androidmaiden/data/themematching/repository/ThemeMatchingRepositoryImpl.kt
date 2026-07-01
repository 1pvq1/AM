package com.example.androidmaiden.data.themematching.repository

import com.example.androidmaiden.domain.themematching.model.*
import com.example.androidmaiden.domain.themematching.repository.ThemeMatchingRepository
import androidx.compose.ui.graphics.Color

/**
 * Concrete implementation of ThemeMatchingRepository supplying static data lists.
 */
class ThemeMatchingRepositoryImpl : ThemeMatchingRepository {

    /**
     * Retrieves the list of available character actions.
     */
    override fun getCharacterActions(): List<CharacterAction> {
        return CharacterAction.entries
    }

    /**
     * Retrieves the list of character stats attributes.
     */
    override fun getCharacterStats(): List<CharacterStats> {
        return listOf(
            CharacterStats("Base Speed", "45 px/s", "±5% (Stepped animation)"),
            CharacterStats("Agility Index", "7.8/10", "Affects acceleration curve"),
            CharacterStats("Animation Rate", "60 fps", "Clamped for pixel art look"),
            CharacterStats("Lively Multiplier", "1.25x", "Based on active state transitions"),
            CharacterStats("Weight Class", "Medium", "Affects jump gravity curve")
        )
    }

    /**
     * Retrieves the list of theme color palette configurations based on active theme colors.
     */
    override fun getThemePalettes(
        primary: Color,
        secondary: Color,
        tertiary: Color,
        error: Color
    ): List<ThemePalette> {
        return listOf(
            ThemePalette("Primary", primary, "Brand base & major UI actions"),
            ThemePalette("Secondary", secondary, "Subtle components & navigation"),
            ThemePalette("Tertiary", tertiary, "Secondary callouts & badge details"),
            ThemePalette("Error Color", error, "Warnings & critical status elements")
        )
    }

    /**
     * Retrieves the list of animation configs for character actions.
     */
    override fun getAnimationConfigs(): List<AnimationConfig> {
        return CharacterAction.entries.map { action ->
            val speed = when (action) {
                CharacterAction.IDLE -> "2500"
                CharacterAction.WALKING -> "800"
                CharacterAction.RUNNING -> "500"
                CharacterAction.WAVING -> "600"
                CharacterAction.JUMPING -> "1800"
                CharacterAction.SLEEPING -> "3000"
            }
            val easing = when (action) {
                CharacterAction.IDLE -> "FastOutSlowIn"
                CharacterAction.WALKING -> "Linear (Stepped)"
                CharacterAction.RUNNING -> "Tween / Stepped"
                CharacterAction.WAVING -> "FastOutSlowIn"
                CharacterAction.JUMPING -> "Launched Sequence"
                CharacterAction.SLEEPING -> "Linear / Slow"
            }
            AnimationConfig(action, speed, true, easing)
        }
    }
}
