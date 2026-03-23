package com.monorepo.core.network.api

import com.monorepo.core.model.ticket.Ticket
import com.monorepo.core.model.user.Installer
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TicketApiService {

    @GET("tickets")
    suspend fun getTickets(
        @Query("partnerId") partnerId: String? = null,
        @Query("installerId") installerId: String? = null,
        @Query("status") status: String? = null,
    ): List<Ticket>

    @GET("tickets/{id}")
    suspend fun getTicketById(@Path("id") id: String): Ticket
}

interface TeamApiService {

    @GET("partners/{partnerId}/installers")
    suspend fun getInstallers(@Path("partnerId") partnerId: String): List<Installer>
}
