package com.example.field_notes.ui.screens.home

import androidx.compose.ui.graphics.Color

val noteColors = listOf (
    0xFFFFFFFF, // white
    0xFFFFCDD2, // red
    0xFFFFE0B2, // orange
    0xFFFFF9C4, // yellow
    0xFFC8E6C9, // green
    0xFFBBDEFB, // blue
    0xFFE1BEE7, // purple
    0xFFD7CCC8, // brown
)

fun Long.toComposeColor(): Color = Color(this)

fun Long.contentColorFor(): Color {
    val color = Color(this)
    val red = color.red * 0.299f
    val green = color.green * 0.587f
    val blue = color.blue * 0.114f
    val luminance = red + green + blue
    return if (luminance > 0.5f) Color.Black else Color.White
}