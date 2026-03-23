package com.monorepo.partner.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.monorepo.feature.auth.navigation.loginScreen
import com.monorepo.feature.home.navigation.HOME_ROUTE
import com.monorepo.feature.home.navigation.homeScreen
import com.monorepo.feature.home.navigation.navigateToHome
import com.monorepo.feature.settings.navigation.SETTINGS_ROUTE
import com.monorepo.feature.settings.navigation.settingsScreen
import com.monorepo.feature.team.navigation.TEAM_ROUTE
import com.monorepo.feature.team.navigation.teamScreen
import com.monorepo.feature.tickets.navigation.ticketsScreen
import com.monorepo.feature.tickets.navigation.TICKETS_ROUTE

private enum class PartnerTopLevel(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    HOME(HOME_ROUTE, "Home", Icons.Default.Home),
    TEAM(TEAM_ROUTE, "My Team", Icons.Default.Group),
    SETTINGS(SETTINGS_ROUTE, "Settings", Icons.Default.Settings),
}

@Composable
fun PartnerNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val topLevelRoutes = PartnerTopLevel.entries.map { it.route }
    val showBottomBar = currentDestination?.route in topLevelRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    PartnerTopLevel.entries.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                            label = { Text(text = screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login/PARTNER",
            modifier = modifier.padding(innerPadding),
        ) {
            loginScreen(
                onLoginSuccess = { navController.navigateToHome() },
            )

            homeScreen(
                onPendingTasksClick = { navController.navigate(TICKETS_ROUTE) },
            )

            ticketsScreen(
                onBackClick = { navController.popBackStack() },
            )

            teamScreen(
                onBackClick = { navController.popBackStack() },
            )

            settingsScreen(
                onBackClick = { navController.popBackStack() },
                onLoggedOut = {
                    navController.navigate("login/PARTNER") {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}
