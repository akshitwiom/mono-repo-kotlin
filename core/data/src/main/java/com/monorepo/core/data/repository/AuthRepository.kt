package com.monorepo.core.data.repository

import com.monorepo.core.common.result.Result
import com.monorepo.core.model.auth.LoginResponse
import com.monorepo.core.model.user.User
import com.monorepo.core.model.user.UserRole
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for authentication operations.
 * Implementations exist for real API and mock flavors.
 */
interface AuthRepository {

    /** Login with username and password. */
    suspend fun loginWithCredentials(
        username: String,
        password: String,
        role: UserRole,
    ): Result<LoginResponse>

    /** Request an OTP to the given phone number. */
    suspend fun requestOtp(phone: String, role: UserRole): Result<Unit>

    /** Verify OTP and login. */
    suspend fun loginWithOtp(
        phone: String,
        otp: String,
        role: UserRole,
    ): Result<LoginResponse>

    /** Observe the currently logged-in user, null when logged out. */
    fun observeCurrentUser(): Flow<User?>

    /** Save the authenticated user to local storage. */
    suspend fun saveUser(user: User)

    /** Clear session and log the user out. */
    suspend fun logout()

    /** Check if a user session exists. */
    suspend fun isLoggedIn(): Boolean
}
