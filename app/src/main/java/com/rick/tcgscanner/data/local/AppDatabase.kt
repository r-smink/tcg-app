package com.rick.tcgscanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PortfolioEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun portfolioDao(): PortfolioDao
}
