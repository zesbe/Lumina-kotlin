package com.luminaai.app.data.api

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Skip auth for login/register/refresh/explore endpoints
        val path = originalRequest.url.encodedPath
        if (path.contains("auth/login") || 
            path.contains("auth/register") || 
            path.contains("auth/refresh") ||
            path.contains("explore")) {
            return chain.proceed(originalRequest)
        }
        
        val token = tokenManager.getAccessTokenSync()
        
        return if (token != null) {
            val authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            
            val response = chain.proceed(authenticatedRequest)
            
            // If 401, try to refresh token
            if (response.code == 401) {
                response.close()
                
                val newToken = runBlocking { refreshToken() }
                if (newToken != null) {
                    val retryRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                    chain.proceed(retryRequest)
                } else {
                    response
                }
            } else {
                response
            }
        } else {
            chain.proceed(originalRequest)
        }
    }
    
    private suspend fun refreshToken(): String? {
        val refreshToken = tokenManager.getRefreshTokenSync() ?: return null
        
        // Note: This is simplified. In production, use a separate OkHttp client
        // to avoid circular dependency
        return try {
            // For now, just return null to force re-login
            // Full implementation would call the refresh endpoint
            null
        } catch (e: Exception) {
            null
        }
    }
}
