package com.monorepo.core.data.mock

import com.monorepo.core.common.result.Result
import com.monorepo.core.data.repository.TicketRepository
import com.monorepo.core.model.ticket.Ticket
import com.monorepo.core.model.ticket.TicketStatus
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock [TicketRepository] with hardcoded sample tickets for offline development.
 */
@Singleton
class MockTicketRepository @Inject constructor() : TicketRepository {

    private val mockTickets = listOf(
        Ticket(
            id = "TKT-001",
            customerName = "Ramesh Sharma",
            customerPhone = "9876543210",
            address = "B-12, Sector 15, Faridabad",
            status = TicketStatus.TODO,
            assignedInstallerId = "installer_001",
            assignedInstallerName = "Rohit Kumar",
            partnerId = "partner_001",
            createdAt = "2026-03-20T10:00:00",
            description = "New broadband connection – 100 Mbps plan",
        ),
        Ticket(
            id = "TKT-002",
            customerName = "Priya Verma",
            customerPhone = "9876543211",
            address = "A-45, Green Valley, Faridabad",
            status = TicketStatus.IN_PROGRESS,
            assignedInstallerId = "installer_001",
            assignedInstallerName = "Rohit Kumar",
            partnerId = "partner_001",
            createdAt = "2026-03-19T14:30:00",
            description = "Fiber setup – 200 Mbps plan",
        ),
        Ticket(
            id = "TKT-003",
            customerName = "Sunil Gupta",
            customerPhone = "9876543212",
            address = "C-78, NIT 3, Faridabad",
            status = TicketStatus.TODO,
            assignedInstallerId = "installer_002",
            assignedInstallerName = "Amit Singh",
            partnerId = "partner_001",
            createdAt = "2026-03-21T09:15:00",
            description = "Router replacement and speed upgrade",
        ),
        Ticket(
            id = "TKT-004",
            customerName = "Kavita Jain",
            customerPhone = "9876543213",
            address = "D-23, Sector 21C, Faridabad",
            status = TicketStatus.TODO,
            assignedInstallerId = "installer_001",
            assignedInstallerName = "Rohit Kumar",
            partnerId = "partner_001",
            createdAt = "2026-03-22T08:00:00",
            description = "New FTTH connection – 50 Mbps plan",
        ),
        Ticket(
            id = "TKT-005",
            customerName = "Deepak Yadav",
            customerPhone = "9876543214",
            address = "E-9, Ashoka Enclave, Faridabad",
            status = TicketStatus.IN_PROGRESS,
            assignedInstallerId = "installer_002",
            assignedInstallerName = "Amit Singh",
            partnerId = "partner_001",
            createdAt = "2026-03-18T11:45:00",
            description = "Cable rerouting after road construction",
        ),
        Ticket(
            id = "TKT-006",
            customerName = "Neha Kapoor",
            customerPhone = "9876543215",
            address = "F-34, Sector 37, Faridabad",
            status = TicketStatus.COMPLETED,
            assignedInstallerId = "installer_001",
            assignedInstallerName = "Rohit Kumar",
            partnerId = "partner_001",
            createdAt = "2026-03-15T16:00:00",
            description = "Connection activation after payment clearance",
        ),
        Ticket(
            id = "TKT-007",
            customerName = "Vikram Mehra",
            customerPhone = "9876543216",
            address = "G-56, Sector 28, Faridabad",
            status = TicketStatus.TODO,
            assignedInstallerId = null,
            assignedInstallerName = null,
            partnerId = "partner_001",
            createdAt = "2026-03-23T07:30:00",
            description = "New connection request – pending assignment",
        ),
    )

    override suspend fun getTicketsForPartner(
        partnerId: String,
        status: TicketStatus?,
    ): Result<List<Ticket>> {
        delay(600)
        val filtered = mockTickets
            .filter { it.partnerId == partnerId }
            .let { list -> if (status != null) list.filter { it.status == status } else list }
        return Result.Success(filtered)
    }

    override suspend fun getTicketsForInstaller(
        installerId: String,
        status: TicketStatus?,
    ): Result<List<Ticket>> {
        delay(600)
        val filtered = mockTickets
            .filter { it.assignedInstallerId == installerId }
            .let { list -> if (status != null) list.filter { it.status == status } else list }
        return Result.Success(filtered)
    }

    override suspend fun getTicketById(id: String): Result<Ticket> {
        delay(400)
        val ticket = mockTickets.find { it.id == id }
        return if (ticket != null) {
            Result.Success(ticket)
        } else {
            Result.Error(NoSuchElementException("Ticket $id not found"), "Ticket not found")
        }
    }

    override suspend fun getPendingTicketCount(userId: String, isPartner: Boolean): Result<Int> {
        delay(400)
        val count = mockTickets.count { ticket ->
            val belongsToUser = if (isPartner) {
                ticket.partnerId == userId
            } else {
                ticket.assignedInstallerId == userId
            }
            belongsToUser && ticket.status in listOf(TicketStatus.TODO, TicketStatus.IN_PROGRESS)
        }
        return Result.Success(count)
    }
}
