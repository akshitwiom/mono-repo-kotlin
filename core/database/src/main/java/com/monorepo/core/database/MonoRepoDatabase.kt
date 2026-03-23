package com.monorepo.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.monorepo.core.database.dao.TicketDao
import com.monorepo.core.database.entity.TicketEntity

@Database(
    entities = [TicketEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class MonoRepoDatabase : RoomDatabase() {
    abstract fun ticketDao(): TicketDao
}
