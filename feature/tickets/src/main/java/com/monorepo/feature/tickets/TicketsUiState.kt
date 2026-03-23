package com.monorepo.feature.tickets

import com.monorepo.core.model.ticket.Ticket
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TicketsUiState(
    val todoTickets: ImmutableList<Ticket> = persistentListOf(),
    val inProgressTickets: ImmutableList<Ticket> = persistentListOf(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val selectedTabIndex: Int = 0,
)
