package com.rick.tcgscanner.data.model

data class PortfolioItem(
    val id: Int = 0,
    val card: Card,
    val quantity: Int = 1,
    val condition: String = "Near Mint",
    val purchasePrice: Float = 0f,
    val notes: String = "",
    val gradePrices: GradePrices = GradePrices(),
    val addedAt: Long = System.currentTimeMillis()
)
