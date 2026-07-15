package com.rick.tcgscanner.data.repository

import android.util.Base64
import com.google.gson.Gson
import com.rick.tcgscanner.data.model.Card
import com.rick.tcgscanner.data.model.ScanResult
import com.rick.tcgscanner.data.remote.kimi.ImageUrl
import com.rick.tcgscanner.data.remote.kimi.ImageUrlPart
import com.rick.tcgscanner.data.remote.kimi.KimiChatRequest
import com.rick.tcgscanner.data.remote.kimi.KimiMessage
import com.rick.tcgscanner.data.remote.kimi.KimiVisionApi
import com.rick.tcgscanner.data.remote.kimi.TextPart
import com.rick.tcgscanner.data.remote.pokemon.PokemonTcgApi
import com.rick.tcgscanner.data.remote.pokemon.toCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class CardRepository(
    private val pokemonApi: PokemonTcgApi,
    private val kimiApi: KimiVisionApi
) {
    private val gson = Gson()

    suspend fun searchCards(scan: ScanResult): Result<List<Card>> = withContext(Dispatchers.IO) {
        try {
            val query = buildString {
                append("""name:\"${scan.name.replace("\"", "\\\"")}\"""")
                if (!scan.setName.isNullOrBlank()) {
                    append(""" set.name:\"${scan.setName.replace("\"", "\\\"")}\"""")
                }
                if (!scan.number.isNullOrBlank()) {
                    append(""" number:\"${scan.number.replace("\"", "\\\"")}\"""")
                }
            }
            val response = pokemonApi.searchCards(query)
            val cards = response.data?.map { it.toCard() } ?: emptyList()
            Result.success(cards)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCard(cardId: String): Result<Card> = withContext(Dispatchers.IO) {
        try {
            val dto = pokemonApi.getCard(cardId).data
            if (dto != null) Result.success(dto.toCard()) else Result.failure(IOException("Card not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun identifyCard(imageBytes: ByteArray, apiKey: String): Result<ScanResult> = withContext(Dispatchers.IO) {
        try {
            if (apiKey.isBlank()) {
                return@withContext Result.failure(IllegalStateException("Kimi API key is missing"))
            }
            val base64 = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
            val imageUrl = "data:image/jpeg;base64,$base64"
            val request = KimiChatRequest(
                messages = listOf(
                    KimiMessage(
                        role = "system",
                        content = listOf(TextPart("You are a Pokémon TCG card identifier."))
                    ),
                    KimiMessage(
                        role = "user",
                        content = listOf(
                            ImageUrlPart(ImageUrl(imageUrl)),
                            TextPart(SCAN_PROMPT)
                        )
                    )
                )
            )
            val response = kimiApi.chat("Bearer $apiKey", request)
            val content = response.choices?.firstOrNull()?.message?.content
                ?: return@withContext Result.failure(IOException("Empty AI response"))
            Result.success(parseScanResult(content))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseScanResult(content: String): ScanResult {
        val cleaned = content.trim()
            .removeSurrounding("```json", "```")
            .removeSurrounding("```", "```")
            .trim()
        val json = if (cleaned.startsWith("{")) {
            cleaned
        } else {
            val start = cleaned.indexOf("{")
            val end = cleaned.lastIndexOf("}")
            if (start >= 0 && end > start) cleaned.substring(start, end + 1) else cleaned
        }
        return gson.fromJson(json, ScanResult::class.java) ?: ScanResult()
    }

    companion object {
        private const val SCAN_PROMPT = "Identify the Pokémon TCG card in the image. " +
                "Return a JSON object with fields: name (English card name), setName, number, rarity, and condition. " +
                "For condition use one of: Mint, Near Mint, Excellent, Good, Played, Poor. " +
                "Return only the JSON object, no other text."
    }
}
