package com.monorepo.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.monorepo.core.database.entity.TicketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {

    @Query("SELECT * FROM tickets WHERE partnerId = :partnerId")
    fun getTicketsByPartner(partnerId: String): Flow<List<TicketEntity>>

    @Query("SELECT * FROM tickets WHERE assignedInstallerId = :installerId")
    fun getTicketsByInstaller(installerId: String): Flow<List<TicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tickets: List<TicketEntity>)

    @Query("DELETE FROM tickets")
    suspend fun deleteAll()
}
