package com.example.aiartexample.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aiartexample.ui.home.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResultViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState= _uiState.asStateFlow()

    fun updateImageResult(imageResult: String){
        _uiState.value = _uiState.value.copy(imageResult = imageResult)
    }

    class ResultViewModelFactory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ResultViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

data class ResultUiState(
    val imageResult: String = "",
    val downloadState: UiState<String> = UiState.Idle
)