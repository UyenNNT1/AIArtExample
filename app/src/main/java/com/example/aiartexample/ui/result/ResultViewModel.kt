package com.example.aiartexample.ui.result

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aiartexample.ui.home.UiState
import com.example.pickphoto.repository.PhotoRepository
import com.example.pickphoto.utils.PhotoRepositoryFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class ResultViewModel(
    private val repository: PhotoRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState= _uiState.asStateFlow()

    fun updateImageResult(imageResult: String){
        _uiState.value = _uiState.value.copy(imageResult = imageResult)
    }

    fun downloadImage(context: Context){
        viewModelScope.launch() {
            _uiState.value = _uiState.value.copy(downloadState = UiState.Loading)
            val sourceFile = File(_uiState.value.imageResult)
            val uri = repository.downloadPhoto(context, sourceFile, "image_${System.currentTimeMillis()}")
            if (uri != null) {
                _uiState.value = _uiState.value.copy(downloadState = UiState.Success(uri.toString()))
            } else {
                _uiState.value = _uiState.value.copy(downloadState = UiState.Error("Download failed"))
            }
        }
    }

    fun resetDownloadState(){
        _uiState.value = _uiState.value.copy(downloadState = UiState.Idle)
    }

    class ResultViewModelFactory(
        val context: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                val repository = PhotoRepositoryFactory.create(context)
                return ResultViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

data class ResultUiState(
    val imageResult: String = "",
    val downloadState: UiState<String> = UiState.Idle
)