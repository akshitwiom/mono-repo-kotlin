package com.monorepo.rohit.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
import com.monorepo.feature.tickets.navigation.TICKETS_ROUTE
import com.monorepo.feature.tickets.navigation.ticketsScreen

private enum class RohitTopLevel(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    HOME(HOME_ROUTE, "Home", Icons.Default.Home),
}

@Composable
fun RohitNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val topLevelRoutes = RohitTopLevel.entries.map { it.route }
    val showBottomBar = currentDestination?.route in topLevelRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    RohitTopLevel.entries.forEach { screen ->
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
            startDestination = "login/INSTALLER",
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
        }
    }
}
