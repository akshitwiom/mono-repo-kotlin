package com.monorepo.core.data.repository

import com.monorepo.core.common.result.Result
import com.monorepo.core.model.user.Installer

/**
 * Repository contract for team management (Partner-only).
 */
interface TeamRepository {

    /** Get all installers (Rohits) under a given partner. */
    suspend fun getInstallers(partnerId: String): Result<List<Installer>>
}
