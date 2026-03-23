package com.monorepo.core.data.mock

import com.monorepo.core.common.result.Result
import com.monorepo.core.data.repository.AuthRepository
import com.monorepo.core.model.auth.LoginResponse
import com.monorepo.core.model.user.User
import com.monorepo.core.model.user.UserRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock [AuthRepository] that simulates login flows without a real backend.
 *
 * Hardcoded credentials:
 * - Partner: username=partner / password=partner123 OR phone=9999900000 / otp=123456
 * - Installer: username=rohit / password=rohit123 OR phone=9999911111 / otp=123456
 */
@Singleton
class MockAuthRepository @Inject constructor() : AuthRepository {

    private val currentUser = MutableStateFlow<User?>(null)

    private val mockPartner = User(
        id = "partner_001",
        name = "Akshit (Partner)",
        phone = "9999900000",
        role = UserRole.PARTNER,
        token = "mock_token_partner_001",
    )

    private val mockInstaller = User(
        id = "installer_001",
        name = "Rohit Kumar",
        phone = "9999911111",
        role = UserRole.INSTALLER,
        token = "mock_token_installer_001",
        partnerId = "partner_001",
    )

    override suspend fun loginWithCredentials(
        username: String,
        password: String,
        role: UserRole,
    ): Result<LoginResponse> {
        delay(800) // simulate network latency
        return when {
            role == UserRole.PARTNER && username == "partner" && password == "partner123" -> {
                currentUser.value = mockPartner
                Result.Success(mockPartner.toLoginResponse())
            }
            role == UserRole.INSTALLER && username == "rohit" && password == "rohit123" -> {
                currentUser.value = mockInstaller
                Result.Success(mockInstaller.toLoginResponse())
            }
            else -> Result.Error(
                IllegalArgumentException("Invalid credentials"),
                "Invalid username or password. Try partner/partner123 or rohit/rohit123.",
            )
        }
    }

    override suspend fun requestOtp(phone: String, role: UserRole): Result<Unit> {
        delay(500)
        return when (phone) {
            "9999900000", "9999911111" -> Result.Success(Unit)
            else -> Result.Error(
                IllegalArgumentException("Unknown phone"),
                "Phone number not registered. Try 9999900000 or 9999911111.",
            )
        }
    }

    override suspend fun loginWithOtp(
        phone: String,
        otp: String,
        role: UserRole,
    ): Result<LoginResponse> {
        delay(800)
        if (otp != "123456") {
            return Result.Error(
                IllegalArgumentException("Invalid OTP"),
                "Invalid OTP. Use 123456 for mock login.",
            )
        }
        return when (phone) {
            "9999900000" -> {
                currentUser.value = mockPartner
                Result.Success(mockPartner.toLoginResponse())
            }
            "9999911111" -> {
                currentUser.value = mockInstaller
                Result.Success(mockInstaller.toLoginResponse())
            }
            else -> Result.Error(
                IllegalArgumentException("Unknown phone"),
                "Phone not registered.",
            )
        }
    }

    override fun observeCurrentUser(): Flow<User?> = currentUser

    override suspend fun saveUser(user: User) {
        currentUser.value = user
    }

    override suspend fun logout() {
        currentUser.value = null
    }

    override suspend fun isLoggedIn(): Boolean = currentUser.value != null

    private fun User.toLoginResponse() = LoginResponse(
        userId = id,
        name = name,
        phone = phone,
        token = token,
        role = role,
        partnerId = partnerId,
    )
}
