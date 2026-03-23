package com.monorepo.core.data.repository

import com.monorepo.core.common.result.Result
import com.monorepo.core.model.ticket.Ticket
import com.monorepo.core.model.ticket.TicketStatus

/**
 * Repository contract for ticket / booking operations.
 */
interface TicketRepository {

    /** Get tickets for a partner — optionally filtered by status. */
    suspend fun getTicketsForPartner(
        partnerId: String,
        status: TicketStatus? = null,
    ): Result<List<Ticket>>

    /** Get tickets assigned to an installer — optionally filtered by status. */
    suspend fun getTicketsForInstaller(
        installerId: String,
        status: TicketStatus? = null,
    ): Result<List<Ticket>>

    /** Get a single ticket by ID. */
    suspend fun getTicketById(id: String): Result<Ticket>

    /** Count pending (TODO + IN_PROGRESS) tickets for a given user. */
    suspend fun getPendingTicketCount(userId: String, isPartner: Boolean): Result<Int>
}
