package com.monorepo.core.data.mock

import com.monorepo.core.common.result.Result
import com.monorepo.core.data.repository.TeamRepository
import com.monorepo.core.model.user.Installer
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock [TeamRepository] with sample installer data.
 */
@Singleton
class MockTeamRepository @Inject constructor() : TeamRepository {

    private val mockInstallers = listOf(
        Installer(
            id = "installer_001",
            name = "Rohit Kumar",
            phone = "9999911111",
            assignedTickets = 3,
            isActive = true,
        ),
        Installer(
            id = "installer_002",
            name = "Amit Singh",
            phone = "9999922222",
            assignedTickets = 2,
            isActive = true,
        ),
        Installer(
            id = "installer_003",
            name = "Vijay Patel",
            phone = "9999933333",
            assignedTickets = 0,
            isActive = true,
        ),
        Installer(
            id = "installer_004",
            name = "Sanjay Mehra",
            phone = "9999944444",
            assignedTickets = 1,
            isActive = false,
        ),
    )

    override suspend fun getInstallers(partnerId: String): Result<List<Installer>> {
        delay(600)
        return Result.Success(mockInstallers)
    }
}
