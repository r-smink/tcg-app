package com.rick.tcgscanner.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {
    @Query("SELECT * FROM portfolio ORDER BY addedAt DESC")
    fun getAll(): Flow<List<PortfolioEntity>>

    @Query("SELECT * FROM portfolio WHERE id = :id")
    suspend fun getById(id: Int): PortfolioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PortfolioEntity): Long

    @Update
    suspend fun update(entity: PortfolioEntity)

    @Delete
    suspend fun delete(entity: PortfolioEntity)
}
