package com.monorepo.core.data.mock

import com.monorepo.core.network.di.TokenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockTokenProvider @Inject constructor(
    private val authRepository: MockAuthRepository,
) : TokenProvider {
    override fun getToken(): String? = null // mock doesn't need real tokens
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MockTokenModule {
    @Binds
    @Singleton
    abstract fun bindTokenProvider(impl: MockTokenProvider): TokenProvider
}
