package com.monorepo.core.model.auth

import com.monorepo.core.model.user.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String? = null,
    val password: String? = null,
    val phone: String? = null,
    val otp: String? = null,
    val role: UserRole,
)

@Serializable
data class OtpRequest(
    val phone: String,
    val role: UserRole,
)

@Serializable
data class LoginResponse(
    val userId: String,
    val name: String,
    val phone: String,
    val token: String,
    val role: UserRole,
    val partnerId: String? = null,
)
