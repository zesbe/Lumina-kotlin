package com.luminaai.app.data.api

import com.luminaai.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    companion object {
        const val BASE_URL = "https://luminaai.zesbe.my.id/api/v1/"
    }
    
    // Auth
    @POST("auth/login")
    suspend fun login(@Body body: Map<String, String>): Response<LoginResponse>
    
    @POST("auth/register")
    suspend fun register(@Body body: Map<String, String>): Response<LoginResponse>
    
    @POST("auth/refresh")
    suspend fun refreshToken(@Body body: Map<String, String>): Response<LoginResponse>
    
    // Profile
    @GET("profile")
    suspend fun getProfile(): Response<ProfileResponse>
    
    // Generations
    @GET("generations")
    suspend fun getGenerations(
        @Query("type") type: String? = null,
        @Query("limit") limit: Int = 50
    ): Response<GenerationsResponse>
    
    @GET("generations/{id}")
    suspend fun getGeneration(@Path("id") id: Int): Response<Generation>
    
    @DELETE("generations/{id}")
    suspend fun deleteGeneration(@Path("id") id: Int): Response<MessageResponse>
    
    @POST("generations/{id}/favorite")
    suspend fun toggleFavorite(@Path("id") id: Int): Response<MessageResponse>
    
    @POST("generations/{id}/public")
    suspend fun togglePublic(@Path("id") id: Int): Response<MessageResponse>
    
    // Generate
    @POST("music/generate")
    suspend fun generateMusic(@Body request: GenerateMusicRequest): Response<GenerateResponse>
    
    // Explore (public, no auth)
    @GET("explore")
    suspend fun getExplore(
        @Query("type") type: String? = null,
        @Query("limit") limit: Int = 50
    ): Response<GenerationsResponse>
}
