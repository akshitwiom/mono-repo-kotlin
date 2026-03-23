package com.monorepo.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.monorepo.feature.auth.login.LoginScreen

const val LOGIN_ROUTE = "login/{role}"

fun NavController.navigateToLogin(role: String) {
    navigate("login/$role") {
        popUpTo(graph.startDestinationId) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.loginScreen(onLoginSuccess: () -> Unit) {
    composable(
        route = LOGIN_ROUTE,
        arguments = listOf(
            navArgument("role") { type = NavType.StringType },
        ),
    ) {
        LoginScreen(onLoginSuccess = onLoginSuccess)
    }
}
