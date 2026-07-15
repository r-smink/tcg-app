package com.rick.tcgscanner.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio")
data class PortfolioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cardId: String,
    val name: String,
    val setName: String,
    val number: String,
    val rarity: String,
    val imageUrl: String,
    val smallImageUrl: String,
    val quantity: Int,
    val condition: String,
    val purchasePrice: Float,
    val notes: String,
    val ungradedMarket: Float,
    val psa9: Float,
    val psa10: Float,
    val cgc95: Float,
    val cgc10: Float,
    val bgs95: Float,
    val addedAt: Long = System.currentTimeMillis()
)
