package com.monorepo.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monorepo.core.common.result.Result
import com.monorepo.core.data.repository.AuthRepository
import com.monorepo.core.data.repository.TicketRepository
import com.monorepo.core.model.user.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val ticketRepository: TicketRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun refresh() {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val user = authRepository.observeCurrentUser().filterNotNull().first()
            _uiState.update { it.copy(userName = user.name) }

            val isPartner = user.role == UserRole.PARTNER
            val userId = if (isPartner) user.id else user.id

            when (val result = ticketRepository.getPendingTicketCount(userId, isPartner)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pendingTaskCount = result.data,
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to load data",
                        )
                    }
                }
                is Result.Loading -> Unit
            }
        }
    }
}
