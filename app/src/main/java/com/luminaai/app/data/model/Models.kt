package com.luminaai.app.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

data class Generation(
    val id: Int,
    val type: String,
    val status: String,
    val title: String,
    val prompt: String? = null,
    val style: String? = null,
    val lyrics: String? = null,
    @SerializedName("output_url") val outputUrl: String? = null,
    @SerializedName("thumbnail_url") val thumbnailUrl: String? = null,
    @SerializedName("is_favorite") val isFavorite: Boolean = false,
    @SerializedName("is_public") val isPublic: Boolean = false,
    @SerializedName("created_at") val createdAt: String? = null,
    val artist: String? = null,
    val album: String? = null,
    val duration: Int? = null,
    val genre: String? = null,
    val mood: String? = null,
    val model: String? = null,
    @SerializedName("creator_name") val creatorName: String? = null
) {
    val fullOutputUrl: String
        get() = if (outputUrl?.startsWith("http") == true) outputUrl 
                else "https://luminaai.zesbe.my.id$outputUrl"
    
    val fullThumbnailUrl: String
        get() = if (thumbnailUrl?.startsWith("http") == true) thumbnailUrl 
                else "https://luminaai.zesbe.my.id$thumbnailUrl"
    
    val displayGenre: String
        get() = style?.split(",")?.firstOrNull()?.trim() ?: genre ?: "AI Music"
    
    val displayArtist: String
        get() = artist ?: "Lumina AI"
    
    val cleanedLyrics: String
        get() = lyrics?.trim() ?: ""
    
    val isFromExplore: Boolean
        get() = creatorName != null && creatorName.isNotEmpty()
}

// API Response wrappers
data class LoginResponse(
    val user: User,
    val tokens: Tokens
)

data class Tokens(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String?
)

data class GenerationsResponse(
    val generations: List<Generation>
)

data class ProfileResponse(
    val user: User
)

data class GenerateMusicRequest(
    val title: String,
    val prompt: String,
    val lyrics: String,
    val style: String? = null,
    val model: String = "music-2.0"
)

data class GenerateResponse(
    val message: String,
    val generation: Generation?
)

data class MessageResponse(
    val message: String
)
