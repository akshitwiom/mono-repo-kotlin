package com.monorepo.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.monorepo.feature.settings.SettingsScreen

const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToSettings() {
    navigate(SETTINGS_ROUTE) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.settingsScreen(
    onBackClick: () -> Unit,
    onLoggedOut: () -> Unit,
) {
    composable(route = SETTINGS_ROUTE) {
        SettingsScreen(
            onBackClick = onBackClick,
            onLoggedOut = onLoggedOut,
        )
    }
}
