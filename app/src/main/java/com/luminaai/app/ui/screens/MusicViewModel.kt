package com.luminaai.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luminaai.app.data.model.Generation
import com.luminaai.app.data.repository.MusicRepository
import com.luminaai.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MusicUiState(
    val generations: List<Generation> = emptyList(),
    val exploreGenerations: List<Generation> = emptyList(),
    val isLoading: Boolean = false,
    val isGenerating: Boolean = false,
    val error: String? = null,
    val currentSong: Generation? = null,
    val isPlaying: Boolean = false
)

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val repository: MusicRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MusicUiState())
    val uiState: StateFlow<MusicUiState> = _uiState.asStateFlow()
    
    init {
        loadGenerations()
        loadExplore()
    }
    
    fun loadGenerations() {
        viewModelScope.launch {
            repository.getGenerations("music").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _uiState.update { it.copy(
                            isLoading = false,
                            generations = result.data ?: emptyList()
                        )}
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = result.message
                        )}
                    }
                }
            }
        }
    }
    
    fun loadExplore() {
        viewModelScope.launch {
            repository.getExplore("music").collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(
                            exploreGenerations = result.data ?: emptyList()
                        )}
                    }
                    else -> {}
                }
            }
        }
    }
    
    fun generateMusic(title: String, prompt: String, lyrics: String, style: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true, error = null) }
            
            when (val result = repository.generateMusic(title, prompt, lyrics, style)) {
                is Resource.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isGenerating = false,
                            generations = listOf(result.data!!) + state.generations
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(
                        isGenerating = false,
                        error = result.message
                    )}
                }
                else -> {}
            }
        }
    }
    
    fun toggleFavorite(id: Int) {
        viewModelScope.launch {
            // Optimistic update
            _uiState.update { state ->
                state.copy(
                    generations = state.generations.map {
                        if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
                    }
                )
            }
            
            when (repository.toggleFavorite(id)) {
                is Resource.Error -> {
                    // Revert on error
                    _uiState.update { state ->
                        state.copy(
                            generations = state.generations.map {
                                if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
                            }
                        )
                    }
                }
                else -> {}
            }
        }
    }
    
    fun togglePublic(id: Int) {
        viewModelScope.launch {
            // Optimistic update
            _uiState.update { state ->
                state.copy(
                    generations = state.generations.map {
                        if (it.id == id) it.copy(isPublic = !it.isPublic) else it
                    }
                )
            }
            
            when (repository.togglePublic(id)) {
                is Resource.Error -> {
                    // Revert on error
                    _uiState.update { state ->
                        state.copy(
                            generations = state.generations.map {
                                if (it.id == id) it.copy(isPublic = !it.isPublic) else it
                            }
                        )
                    }
                }
                else -> {}
            }
        }
    }
    
    fun deleteGeneration(id: Int) {
        viewModelScope.launch {
            val backup = _uiState.value.generations.find { it.id == id }
            
            // Optimistic delete
            _uiState.update { state ->
                state.copy(generations = state.generations.filter { it.id != id })
            }
            
            when (repository.deleteGeneration(id)) {
                is Resource.Error -> {
                    // Revert on error
                    if (backup != null) {
                        _uiState.update { state ->
                            state.copy(generations = state.generations + backup)
                        }
                    }
                }
                else -> {}
            }
        }
    }
    
    fun playSong(song: Generation) {
        _uiState.update { it.copy(currentSong = song, isPlaying = true) }
    }
    
    fun togglePlay() {
        _uiState.update { it.copy(isPlaying = !it.isPlaying) }
    }
    
    fun stopPlaying() {
        _uiState.update { it.copy(currentSong = null, isPlaying = false) }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
