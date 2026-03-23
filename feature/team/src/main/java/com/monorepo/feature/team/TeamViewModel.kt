package com.monorepo.feature.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monorepo.core.common.result.Result
import com.monorepo.core.data.repository.AuthRepository
import com.monorepo.core.data.repository.TeamRepository
import com.monorepo.core.model.user.Installer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TeamUiState(
    val installers: ImmutableList<Installer> = persistentListOf(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val teamRepository: TeamRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamUiState())
    val uiState: StateFlow<TeamUiState> = _uiState.asStateFlow()

    init {
        loadInstallers()
    }

    fun refresh() {
        loadInstallers()
    }

    private fun loadInstallers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val user = authRepository.observeCurrentUser().filterNotNull().first()

            when (val result = teamRepository.getInstallers(user.id)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            installers = result.data.toImmutableList(),
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message ?: "Failed to load team")
                    }
                }
                is Result.Loading -> Unit
            }
        }
    }
}
