package com.monorepo.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.monorepo.feature.home.HomeScreen

const val HOME_ROUTE = "home"

fun NavController.navigateToHome() {
    navigate(HOME_ROUTE) {
        popUpTo(graph.startDestinationId) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.homeScreen(onPendingTasksClick: () -> Unit) {
    composable(route = HOME_ROUTE) {
        HomeScreen(onPendingTasksClick = onPendingTasksClick)
    }
}
