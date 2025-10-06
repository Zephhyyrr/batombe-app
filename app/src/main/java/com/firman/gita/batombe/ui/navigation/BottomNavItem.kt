package com.firman.gita.batombe.ui.navigation

import androidx.compose.ui.graphics.painter.Painter

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Painter? = null,
    val isMainFeature: Boolean = false
)