package com.monorepo.core.data.mock

import com.monorepo.core.data.repository.AuthRepository
import com.monorepo.core.data.repository.TeamRepository
import com.monorepo.core.data.repository.TicketRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides mock repository implementations.
 * Used in the 'mock' build flavor for offline-first development.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class MockDataModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: MockAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTicketRepository(impl: MockTicketRepository): TicketRepository

    @Binds
    @Singleton
    abstract fun bindTeamRepository(impl: MockTeamRepository): TeamRepository
}
