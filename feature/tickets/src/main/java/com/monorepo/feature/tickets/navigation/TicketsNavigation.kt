package com.monorepo.feature.tickets.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.monorepo.feature.tickets.TicketsScreen

const val TICKETS_ROUTE = "tickets"

fun NavController.navigateToTickets() {
    navigate(TICKETS_ROUTE) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.ticketsScreen(onBackClick: () -> Unit) {
    composable(route = TICKETS_ROUTE) {
        TicketsScreen(onBackClick = onBackClick)
    }
}
