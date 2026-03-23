package com.monorepo.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey val id: String,
    val customerName: String,
    val customerPhone: String,
    val address: String,
    val status: String,
    val assignedInstallerId: String?,
    val assignedInstallerName: String?,
    val partnerId: String,
    val createdAt: String,
    val description: String,
)
