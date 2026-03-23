package com.monorepo.core.database.di

import android.content.Context
import androidx.room.Room
import com.monorepo.core.database.MonoRepoDatabase
import com.monorepo.core.database.dao.TicketDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MonoRepoDatabase =
        Room.databaseBuilder(
            context,
            MonoRepoDatabase::class.java,
            "monorepo_db",
        ).build()

    @Provides
    fun provideTicketDao(database: MonoRepoDatabase): TicketDao =
        database.ticketDao()
}
