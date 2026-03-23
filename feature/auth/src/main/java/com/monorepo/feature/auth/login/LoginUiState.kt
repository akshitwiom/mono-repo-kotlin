package com.monorepo.feature.auth.login

import com.monorepo.core.model.user.UserRole

/**
 * Sealed hierarchy for the login screen UI state.
 */
data class LoginUiState(
    val loginMode: LoginMode = LoginMode.PHONE_OTP,
    val phone: String = "",
    val otp: String = "",
    val username: String = "",
    val password: String = "",
    val isOtpSent: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false,
    val role: UserRole = UserRole.PARTNER,
)

enum class LoginMode {
    PHONE_OTP,
    USERNAME_PASSWORD,
}

sealed interface LoginUiEvent {
    data class PhoneChanged(val phone: String) : LoginUiEvent
    data class OtpChanged(val otp: String) : LoginUiEvent
    data class UsernameChanged(val username: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    data object ToggleLoginMode : LoginUiEvent
    data object RequestOtp : LoginUiEvent
    data object SubmitLogin : LoginUiEvent
    data object ClearError : LoginUiEvent
}
