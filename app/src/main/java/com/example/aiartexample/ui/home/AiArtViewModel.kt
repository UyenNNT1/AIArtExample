package com.example.aiartexample.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aiartexample.mapper.toModel
import com.example.aiartexample.model.AiArtCategory
import com.example.aiartexample.model.AiArtStyle
import com.example.aiartservice.network.model.AiArtParams
import com.example.aiartservice.network.repository.aiartv5.AiArtRepository
import com.example.aiartservice.network.repository.aistyle.AiStyleRepository
import com.example.aiartservice.network.response.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AiArtStyleViewModel(
    private val aiStyleRepository: AiStyleRepository,
    private val aiGenRepository: AiArtRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AiArtStyleUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchAiArtCategories()
    }

    private fun fetchAiArtCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(categoriesState = UiState.Loading) }
            when (val result = aiStyleRepository.getAiStyles("IN")) {
                is ResponseState.Success -> {
                    val data = result.data
                    val categories = data?.map { categoryResponse -> categoryResponse.toModel() }.orEmpty()
                    _uiState.update {
                        it.copy(
                            categoriesState = UiState.Success(categories),
                            currentCategoryIndex = 0,
                            currentStyleIndex = -1
                        )
                    }
                }

                else -> UiState.Error("Failed to load data")
            }
        }
    }

    fun genAiArtImage(params: AiArtParams) {
        viewModelScope.launch {
            _uiState.update { it.copy(generatedImageState = UiState.Loading) }
            when (val res = aiGenRepository.genArtAi(params)) {
                is ResponseState.Success -> {
                    _uiState.update { it.copy(generatedImageState = UiState.Success(res.data?.path!!)) }
                }

                is ResponseState.Error -> {
                    _uiState.update { it.copy(generatedImageState = UiState.Error(res.error.message)) }
                }
            }
        }
    }

    fun updateCategoryIndex(index: Int) {
        _uiState.update {
            it.copy(currentCategoryIndex = index, currentStyleIndex = -1)
        }
    }

    fun updateStyleIndex(index: Int) {
        _uiState.update {
            it.copy(currentStyleIndex = index)
        }
    }

    fun updateOriginalImage(imageUri: String) {
        _uiState.update {
            it.copy(originalImage = imageUri)
        }
    }

    fun resetGeneratedImageState() {
        _uiState.update {
            it.copy(generatedImageState = UiState.Idle)
        }
    }

    class AiStyleViewModelFactory(
        private val aiStyleRepository: AiStyleRepository,
        private val aiGenRepository: AiArtRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AiArtStyleViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AiArtStyleViewModel(aiStyleRepository, aiGenRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

data class AiArtStyleUiState(
    val categoriesState: UiState<List<AiArtCategory>> = UiState.Loading,
    val currentCategoryIndex: Int = 0,
    val currentStyleIndex: Int = 0,
    val originalImage: String? = null,
    val generatedImageState: UiState<String> = UiState.Idle
)

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String?) : UiState<Nothing>()
    object Idle : UiState<Nothing>()
}