package com.rick.tcgscanner.data.di

import android.content.Context
import androidx.room.Room
import com.rick.tcgscanner.data.local.AppDatabase
import com.rick.tcgscanner.data.remote.kimi.KimiVisionApi
import com.rick.tcgscanner.data.remote.pokemon.PokemonTcgApi
import com.rick.tcgscanner.data.repository.CardRepository
import com.rick.tcgscanner.data.repository.PortfolioRepository
import com.rick.tcgscanner.data.repository.SettingsRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(context: Context) {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private fun createRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val pokemonApi: PokemonTcgApi by lazy {
        createRetrofit("https://api.pokemontcg.io/").create(PokemonTcgApi::class.java)
    }

    private val kimiApi: KimiVisionApi by lazy {
        createRetrofit("https://api.moonshot.ai/").create(KimiVisionApi::class.java)
    }

    private val database by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "tcg_db").build()
    }

    val cardRepository: CardRepository by lazy { CardRepository(pokemonApi, kimiApi) }
    val portfolioRepository: PortfolioRepository by lazy { PortfolioRepository(database.portfolioDao()) }
    val settingsRepository: SettingsRepository by lazy { SettingsRepository(context) }
}
