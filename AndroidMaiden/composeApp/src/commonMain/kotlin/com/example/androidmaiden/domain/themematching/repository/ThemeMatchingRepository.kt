package com.example.androidmaiden.domain.themematching.repository

import com.example.androidmaiden.domain.themematching.model.*
import androidx.compose.ui.graphics.Color

/**
 * Repository interface for managing data related to Theme Matching.
 */
interface ThemeMatchingRepository {
    /**
     * Retrieves the list of available character actions.
     */
    fun getCharacterActions(): List<CharacterAction>

    /**
     * Retrieves the list of character stats attributes.
     */
    fun getCharacterStats(): List<CharacterStats>

    /**
     * Retrieves the list of theme color palette configurations.
     */
    fun getThemePalettes(
        primary: Color,
        secondary: Color,
        tertiary: Color,
        error: Color
    ): List<ThemePalette>

    /**
     * Retrieves the list of animation configs for character actions.
     */
    fun getAnimationConfigs(): List<AnimationConfig>
}
