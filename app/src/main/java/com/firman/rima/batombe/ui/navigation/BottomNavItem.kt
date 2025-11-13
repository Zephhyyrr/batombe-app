package com.firman.rima.batombe.ui.navigation

import androidx.compose.ui.graphics.painter.Painter

data class BottomNavItem(
    val route: String,
    val title: String? = null,
    val icon: Painter? = null,
    val isMainFeature: Boolean = false,
    val isEnabled: Boolean = true
)