package com.example.androidmaiden.presentation.ui.adaptive

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class WindowSizeClassTest {

    @Test
    fun testCompactWidth() {
        val windowSize = WindowSizeClass.calculate(599.dp, 1000.dp)
        assertEquals(WindowSizeCategory.Compact, windowSize.widthCategory)
    }

    @Test
    fun testMediumWidth() {
        val windowSize = WindowSizeClass.calculate(600.dp, 1000.dp)
        assertEquals(WindowSizeCategory.Medium, windowSize.widthCategory)
        
        val windowSize2 = WindowSizeClass.calculate(839.dp, 1000.dp)
        assertEquals(WindowSizeCategory.Medium, windowSize2.widthCategory)
    }

    @Test
    fun testExpandedWidth() {
        val windowSize = WindowSizeClass.calculate(840.dp, 1000.dp)
        assertEquals(WindowSizeCategory.Expanded, windowSize.widthCategory)
    }

    @Test
    fun testCompactHeight() {
        val windowSize = WindowSizeClass.calculate(1000.dp, 479.dp)
        assertEquals(WindowSizeCategory.Compact, windowSize.heightCategory)
    }

    @Test
    fun testMediumHeight() {
        val windowSize = WindowSizeClass.calculate(1000.dp, 480.dp)
        assertEquals(WindowSizeCategory.Medium, windowSize.heightCategory)
        
        val windowSize2 = WindowSizeClass.calculate(1000.dp, 899.dp)
        assertEquals(WindowSizeCategory.Medium, windowSize2.heightCategory)
    }

    @Test
    fun testExpandedHeight() {
        val windowSize = WindowSizeClass.calculate(1000.dp, 900.dp)
        assertEquals(WindowSizeCategory.Expanded, windowSize.heightCategory)
    }
}
