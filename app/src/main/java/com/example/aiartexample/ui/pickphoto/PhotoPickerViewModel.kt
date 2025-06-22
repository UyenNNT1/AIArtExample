package com.example.aiartexample.ui.pickphoto

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aiartexample.ui.home.AiArtStyleViewModel
import com.example.aiartservice.network.repository.aiartv5.AiArtRepository
import com.example.aiartservice.network.repository.aistyle.AiStyleRepository
import com.example.pickphoto.model.FolderData
import com.example.pickphoto.model.PhotoData
import com.example.pickphoto.repository.PhotoRepository
import com.example.pickphoto.utils.PermissionUtils
import com.example.pickphoto.utils.PhotoRepositoryFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PhotoPickerUiState(
    val isLoading: Boolean = false,
    val photos: List<PhotoData> = emptyList(),
    val selectedPhoto: PhotoData? = null
)

class PhotoPickerViewModel(
    private val repository: PhotoRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PhotoPickerUiState())
    val uiState: StateFlow<PhotoPickerUiState> = _uiState.asStateFlow()
    
    init {
        checkPermissionAndLoadPhotos()
    }
    
    fun checkPermissionAndLoadPhotos() {
        loadAllPhotos()
    }
    
    fun loadAllPhotos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val photos = repository.getAllPhotos()
                _uiState.value = _uiState.value.copy(
                    photos = photos,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false
                )
            }
        }
    }

    fun updateSelectedPhoto(photo: PhotoData) {
        _uiState.value = _uiState.value.copy(
            selectedPhoto = photo
        )
    }

    class PickPhotoViewModelFactory(
        private val context: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PhotoPickerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                val repository = PhotoRepositoryFactory.create(context)
                return PhotoPickerViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
