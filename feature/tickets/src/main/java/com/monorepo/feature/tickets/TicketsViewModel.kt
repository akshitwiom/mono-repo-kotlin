package com.monorepo.feature.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monorepo.core.common.result.Result
import com.monorepo.core.data.repository.AuthRepository
import com.monorepo.core.data.repository.TicketRepository
import com.monorepo.core.model.ticket.TicketStatus
import com.monorepo.core.model.user.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val ticketRepository: TicketRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketsUiState())
    val uiState: StateFlow<TicketsUiState> = _uiState.asStateFlow()

    init {
        loadTickets()
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    fun refresh() {
        loadTickets()
    }

    private fun loadTickets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val user = authRepository.observeCurrentUser().filterNotNull().first()
            val isPartner = user.role == UserRole.PARTNER

            val todoResult = if (isPartner) {
                ticketRepository.getTicketsForPartner(user.id, TicketStatus.TODO)
            } else {
                ticketRepository.getTicketsForInstaller(user.id, TicketStatus.TODO)
            }

            val inProgressResult = if (isPartner) {
                ticketRepository.getTicketsForPartner(user.id, TicketStatus.IN_PROGRESS)
            } else {
                ticketRepository.getTicketsForInstaller(user.id, TicketStatus.IN_PROGRESS)
            }

            when {
                todoResult is Result.Success && inProgressResult is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            todoTickets = todoResult.data.toImmutableList(),
                            inProgressTickets = inProgressResult.data.toImmutableList(),
                        )
                    }
                }
                todoResult is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = todoResult.message ?: "Failed to load tickets")
                    }
                }
                inProgressResult is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = inProgressResult.message ?: "Failed to load tickets")
                    }
                }
                else -> Unit
            }
        }
    }
}
