package com.monorepo.core.model.ticket

import kotlinx.serialization.Serializable

/**
 * Domain model for a connection setup ticket / booking.
 */
@Serializable
data class Ticket(
    val id: String,
    val customerName: String,
    val customerPhone: String,
    val address: String,
    val status: TicketStatus,
    val assignedInstallerId: String? = null,
    val assignedInstallerName: String? = null,
    val partnerId: String,
    val createdAt: String = "",
    val description: String = "",
)
