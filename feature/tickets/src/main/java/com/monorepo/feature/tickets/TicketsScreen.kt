package com.monorepo.feature.tickets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.monorepo.core.designsystem.component.ErrorScreen
import com.monorepo.core.designsystem.component.LoadingIndicator
import com.monorepo.core.model.ticket.Ticket
import com.monorepo.core.ui.TicketListItem
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TicketsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Tickets") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { padding ->
        when {
            uiState.isLoading -> LoadingIndicator(modifier = Modifier.padding(padding))
            uiState.errorMessage != null -> ErrorScreen(
                message = uiState.errorMessage.orEmpty(),
                onRetry = viewModel::refresh,
                modifier = Modifier.padding(padding),
            )
            else -> TicketsContent(
                uiState = uiState,
                onTabSelected = viewModel::onTabSelected,
                modifier = modifier.padding(padding),
            )
        }
    }
}

@Composable
private fun TicketsContent(
    uiState: TicketsUiState,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf("To Do (${uiState.todoTickets.size})", "In Progress (${uiState.inProgressTickets.size})")

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = uiState.selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = uiState.selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = { Text(text = title) },
                )
            }
        }

        val tickets: ImmutableList<Ticket> = when (uiState.selectedTabIndex) {
            0 -> uiState.todoTickets
            1 -> uiState.inProgressTickets
            else -> uiState.todoTickets
        }

        if (tickets.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "No tickets here",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = tickets, key = { it.id }) { ticket ->
                    TicketListItem(ticket = ticket)
                }
            }
        }
    }
}
