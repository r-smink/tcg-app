package com.rick.tcgscanner.data.remote.pokemon

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonTcgApi {
    @GET("v2/cards")
    suspend fun searchCards(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int = 20
    ): PokemonTcgResponse

    @GET("v2/cards/{id}")
    suspend fun getCard(@Path("id") id: String): PokemonTcgCardResponse
}

data class PokemonTcgResponse(
    val data: List<PokemonCardDto>? = null,
    val page: Int = 0,
    val pageSize: Int = 0,
    val count: Int = 0,
    val totalCount: Int = 0
)

data class PokemonTcgCardResponse(
    val data: PokemonCardDto? = null
)

data class PokemonCardDto(
    val id: String,
    val name: String,
    val supertype: String? = null,
    val subtypes: List<String>? = null,
    val hp: String? = null,
    val types: List<String>? = null,
    val number: String? = null,
    val rarity: String? = null,
    val set: PokemonSetDto? = null,
    val images: PokemonImagesDto? = null,
    val tcgplayer: PokemonTcgPlayerDto? = null
)

data class PokemonSetDto(
    val id: String? = null,
    val name: String? = null,
    val series: String? = null
)

data class PokemonImagesDto(
    val small: String? = null,
    val large: String? = null
)

data class PokemonTcgPlayerDto(
    val normal: PokemonPriceDto? = null,
    val holofoil: PokemonPriceDto? = null,
    val reverseHolofoil: PokemonPriceDto? = null
)

data class PokemonPriceDto(
    val low: Float? = null,
    val mid: Float? = null,
    val high: Float? = null,
    val market: Float? = null,
    val directLow: Float? = null
)
