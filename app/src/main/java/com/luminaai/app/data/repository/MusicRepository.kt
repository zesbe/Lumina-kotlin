package com.luminaai.app.data.repository

import com.luminaai.app.data.api.ApiService
import com.luminaai.app.data.api.TokenManager
import com.luminaai.app.data.model.*
import com.luminaai.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    // Auth
    suspend fun login(email: String, password: String): Resource<User> {
        return try {
            val response = apiService.login(mapOf("email" to email, "password" to password))
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                tokenManager.saveTokens(data.tokens.accessToken, data.tokens.refreshToken)
                Resource.Success(data.user)
            } else {
                Resource.Error(response.message() ?: "Login failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
    
    suspend fun register(name: String, email: String, password: String): Resource<User> {
        return try {
            val response = apiService.register(mapOf(
                "name" to name,
                "email" to email,
                "password" to password
            ))
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                tokenManager.saveTokens(data.tokens.accessToken, data.tokens.refreshToken)
                Resource.Success(data.user)
            } else {
                Resource.Error(response.message() ?: "Registration failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
    
    suspend fun getProfile(): Resource<User> {
        return try {
            val response = apiService.getProfile()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.user)
            } else {
                Resource.Error(response.message() ?: "Failed to get profile")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
    
    suspend fun logout() {
        tokenManager.clearTokens()
    }
    
    suspend fun isLoggedIn(): Boolean = tokenManager.hasToken()
    
    // Generations
    fun getGenerations(type: String? = "music"): Flow<Resource<List<Generation>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getGenerations(type)
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!.generations))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to load"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }
    
    fun getExplore(type: String? = "music"): Flow<Resource<List<Generation>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getExplore(type)
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!.generations))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to load"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }
    
    suspend fun toggleFavorite(id: Int): Resource<Boolean> {
        return try {
            val response = apiService.toggleFavorite(id)
            if (response.isSuccessful) {
                Resource.Success(true)
            } else {
                Resource.Error(response.message() ?: "Failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
    
    suspend fun togglePublic(id: Int): Resource<Boolean> {
        return try {
            val response = apiService.togglePublic(id)
            if (response.isSuccessful) {
                Resource.Success(true)
            } else {
                Resource.Error(response.message() ?: "Failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
    
    suspend fun deleteGeneration(id: Int): Resource<Boolean> {
        return try {
            val response = apiService.deleteGeneration(id)
            if (response.isSuccessful) {
                Resource.Success(true)
            } else {
                Resource.Error(response.message() ?: "Failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
    
    suspend fun generateMusic(
        title: String,
        prompt: String,
        lyrics: String,
        style: String?
    ): Resource<Generation> {
        return try {
            val request = GenerateMusicRequest(title, prompt, lyrics, style)
            val response = apiService.generateMusic(request)
            if (response.isSuccessful && response.body()?.generation != null) {
                Resource.Success(response.body()!!.generation!!)
            } else {
                Resource.Error(response.message() ?: "Failed to generate")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
