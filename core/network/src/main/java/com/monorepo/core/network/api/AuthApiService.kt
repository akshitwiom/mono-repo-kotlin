package com.monorepo.core.network.api

import com.monorepo.core.model.auth.LoginRequest
import com.monorepo.core.model.auth.LoginResponse
import com.monorepo.core.model.auth.OtpRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/otp/request")
    suspend fun requestOtp(@Body request: OtpRequest)

    @POST("auth/otp/verify")
    suspend fun verifyOtp(@Body request: LoginRequest): LoginResponse
}
