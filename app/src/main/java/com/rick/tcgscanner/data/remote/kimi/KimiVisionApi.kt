package com.rick.tcgscanner.data.remote.kimi

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface KimiVisionApi {
    @POST("v1/chat/completions")
    suspend fun chat(
        @Header("Authorization") authorization: String,
        @Body request: KimiChatRequest
    ): KimiChatResponse
}

data class KimiChatRequest(
    val model: String = "kimi-k2.6",
    val messages: List<KimiMessage>,
    val temperature: Float = 0.2f,
    val max_tokens: Int = 512
)

data class KimiMessage(
    val role: String,
    val content: List<KimiContentPart>
)

sealed class KimiContentPart {
    abstract val type: String
}

data class TextPart(val text: String) : KimiContentPart() {
    override val type: String = "text"
}

data class ImageUrlPart(val image_url: ImageUrl) : KimiContentPart() {
    override val type: String = "image_url"
}

data class ImageUrl(val url: String)

data class KimiChatResponse(
    val id: String? = null,
    val `object`: String? = null,
    val created: Long? = null,
    val model: String? = null,
    val choices: List<KimiChoice>? = null,
    val usage: KimiUsage? = null
)

data class KimiChoice(
    val index: Int? = null,
    val message: KimiResponseMessage? = null,
    val finish_reason: String? = null
)

data class KimiResponseMessage(
    val role: String? = null,
    val content: String? = null
)

data class KimiUsage(
    val prompt_tokens: Int? = null,
    val completion_tokens: Int? = null,
    val total_tokens: Int? = null
)
