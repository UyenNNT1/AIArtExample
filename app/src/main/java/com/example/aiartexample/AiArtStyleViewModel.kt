package com.example.aiartexample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aiartexample.mapper.toModel
import com.example.aiartexample.model.AiArtCategory
import com.example.aiartservice.network.repository.aistyle.AiStyleRepository
import com.example.aiartservice.network.response.ResponseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AiArtStyleViewModel(private val aiStyleRepository: AiStyleRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AiArtStyleUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            when (val result = aiStyleRepository.getAiStyles("IN")) {
                is ResponseState.Success -> {
                    val data = result.data
                    val categories = data?.map { categoryResponse -> categoryResponse.toModel() }.orEmpty()
                    _uiState.update {
                        it.copy(categories = categories)
                    }
                }

                else -> Unit
            }
        }
    }

    class AiStyleViewModelFactory(
        private val aiStyleRepository: AiStyleRepository,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AiArtStyleViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AiArtStyleViewModel(aiStyleRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

data class AiArtStyleUiState(
    val categories: List<AiArtCategory> = emptyList(),
)