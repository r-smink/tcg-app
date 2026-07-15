package com.rick.tcgscanner.data.model

data class ScanResult(
    val name: String = "",
    val setName: String? = null,
    val number: String? = null,
    val rarity: String? = null,
    val condition: String? = null
)
