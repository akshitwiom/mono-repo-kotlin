package com.monorepo.core.model.user

import kotlinx.serialization.Serializable

/**
 * Domain model representing an authenticated user.
 * Used across both Partner and Rohit apps.
 */
@Serializable
data class User(
    val id: String,
    val name: String,
    val phone: String,
    val role: UserRole,
    val token: String = "",
    val partnerId: String? = null,
)
