package com.monorepo.core.model.ticket

import kotlinx.serialization.Serializable

@Serializable
enum class TicketStatus {
    TODO,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
}
