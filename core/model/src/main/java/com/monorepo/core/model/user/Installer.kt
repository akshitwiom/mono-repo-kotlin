package com.monorepo.core.model.user

import kotlinx.serialization.Serializable

/**
 * Domain model for an installer (Rohit) belonging to a Partner's team.
 */
@Serializable
data class Installer(
    val id: String,
    val name: String,
    val phone: String,
    val assignedTickets: Int = 0,
    val isActive: Boolean = true,
)
