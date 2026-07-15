package com.rick.tcgscanner.data.model

data class Card(
    val id: String,
    val name: String,
    val supertype: String? = null,
    val subtypes: List<String> = emptyList(),
    val hp: String? = null,
    val types: List<String> = emptyList(),
    val number: String? = null,
    val rarity: String? = null,
    val setName: String? = null,
    val setSeries: String? = null,
    val smallImage: String = "",
    val largeImage: String = "",
    val tcgplayerPrices: TcgPlayerPrices? = null
)

data class TcgPlayerPrices(
    val normal: Price? = null,
    val holofoil: Price? = null,
    val reverseHolofoil: Price? = null
)

data class Price(
    val low: Float? = null,
    val mid: Float? = null,
    val high: Float? = null,
    val market: Float? = null,
    val directLow: Float? = null
)
