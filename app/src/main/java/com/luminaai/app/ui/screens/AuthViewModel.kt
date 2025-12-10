package com.luminaai.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luminaai.app.data.model.User
import com.luminaai.app.data.repository.MusicRepository
import com.luminaai.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: MusicRepository
) : ViewModel() {
    
    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()
    
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        checkAuth()
    }
    
    private fun checkAuth() {
        viewModelScope.launch {
            val loggedIn = repository.isLoggedIn()
            if (loggedIn) {
                when (val result = repository.getProfile()) {
                    is Resource.Success -> {
                        _user.value = result.data
                        _isLoggedIn.value = true
                    }
                    is Resource.Error -> {
                        _isLoggedIn.value = false
                    }
                    else -> {}
                }
            } else {
                _isLoggedIn.value = false
            }
        }
    }
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            when (val result = repository.login(email, password)) {
                is Resource.Success -> {
                    _user.value = result.data
                    _isLoggedIn.value = true
                }
                is Resource.Error -> {
                    _error.value = result.message
                }
                else -> {}
            }
            
            _isLoading.value = false
        }
    }
    
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            when (val result = repository.register(name, email, password)) {
                is Resource.Success -> {
                    _user.value = result.data
                    _isLoggedIn.value = true
                }
                is Resource.Error -> {
                    _error.value = result.message
                }
                else -> {}
            }
            
            _isLoading.value = false
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _user.value = null
            _isLoggedIn.value = false
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
