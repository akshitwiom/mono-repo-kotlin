package com.monorepo.feature.auth.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monorepo.core.common.extension.isValidOtp
import com.monorepo.core.common.extension.isValidPassword
import com.monorepo.core.common.extension.isValidPhoneNumber
import com.monorepo.core.common.extension.isValidUsername
import com.monorepo.core.common.result.Result
import com.monorepo.core.data.repository.AuthRepository
import com.monorepo.core.model.user.User
import com.monorepo.core.model.user.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val role: UserRole = savedStateHandle.get<String>("role")
        ?.let { UserRole.valueOf(it) }
        ?: UserRole.PARTNER

    private val _uiState = MutableStateFlow(LoginUiState(role = role))
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.PhoneChanged -> _uiState.update { it.copy(phone = event.phone, errorMessage = null) }
            is LoginUiEvent.OtpChanged -> _uiState.update { it.copy(otp = event.otp, errorMessage = null) }
            is LoginUiEvent.UsernameChanged -> _uiState.update { it.copy(username = event.username, errorMessage = null) }
            is LoginUiEvent.PasswordChanged -> _uiState.update { it.copy(password = event.password, errorMessage = null) }
            is LoginUiEvent.ToggleLoginMode -> toggleMode()
            is LoginUiEvent.RequestOtp -> requestOtp()
            is LoginUiEvent.SubmitLogin -> submitLogin()
            is LoginUiEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun toggleMode() {
        _uiState.update { state ->
            val newMode = when (state.loginMode) {
                LoginMode.PHONE_OTP -> LoginMode.USERNAME_PASSWORD
                LoginMode.USERNAME_PASSWORD -> LoginMode.PHONE_OTP
            }
            state.copy(loginMode = newMode, errorMessage = null)
        }
    }

    private fun requestOtp() {
        val phone = _uiState.value.phone
        if (!phone.isValidPhoneNumber()) {
            _uiState.update { it.copy(errorMessage = "Enter a valid 10-digit phone number") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = authRepository.requestOtp(phone, role)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, isOtpSent = true) }
                is Result.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message ?: "Failed to send OTP") }
                is Result.Loading -> Unit
            }
        }
    }

    private fun submitLogin() {
        val state = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = when (state.loginMode) {
                LoginMode.PHONE_OTP -> {
                    if (!state.phone.isValidPhoneNumber()) {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Enter a valid phone number") }
                        return@launch
                    }
                    if (!state.otp.isValidOtp()) {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Enter a valid 6-digit OTP") }
                        return@launch
                    }
                    authRepository.loginWithOtp(state.phone, state.otp, role)
                }

                LoginMode.USERNAME_PASSWORD -> {
                    if (!state.username.isValidUsername()) {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Username must be 3–30 alphanumeric characters") }
                        return@launch
                    }
                    if (!state.password.isValidPassword()) {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Password must be at least 6 characters") }
                        return@launch
                    }
                    authRepository.loginWithCredentials(state.username, state.password, role)
                }
            }

            when (result) {
                is Result.Success -> {
                    val response = result.data
                    val user = User(
                        id = response.userId,
                        name = response.name,
                        phone = response.phone,
                        role = response.role,
                        token = response.token,
                        partnerId = response.partnerId,
                    )
                    authRepository.saveUser(user)
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message ?: "Login failed") }
                }
                is Result.Loading -> Unit
            }
        }
    }
}
