package com.monorepo.feature.team.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.monorepo.feature.team.TeamScreen

const val TEAM_ROUTE = "team"

fun NavController.navigateToTeam() {
    navigate(TEAM_ROUTE) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.teamScreen(onBackClick: () -> Unit) {
    composable(route = TEAM_ROUTE) {
        TeamScreen(onBackClick = onBackClick)
    }
}
