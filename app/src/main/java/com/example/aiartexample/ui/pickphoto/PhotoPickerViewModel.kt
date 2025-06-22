package com.example.aiartexample.ui.pickphoto

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pickphoto.model.PhotoData
import com.example.pickphoto.repository.PhotoRepository
import com.example.pickphoto.utils.PhotoRepositoryFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flatMapLatest

data class PhotoPickerUiState(
    val isLoading: Boolean = false,
    val selectedPhoto: PhotoData? = null
)

class PhotoPickerViewModel(
    private val repository: PhotoRepository
) : ViewModel() {
    private val _permissionGrantedKey = MutableStateFlow(0)
    val permissionGrantedKey = _permissionGrantedKey.asStateFlow()

    val photoPagingFlow: Flow<PagingData<PhotoData>> =
        permissionGrantedKey.flatMapLatest {
            repository.getPhotoPagingFlow(pageSize = 50)
        }.cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(PhotoPickerUiState())
    val uiState: StateFlow<PhotoPickerUiState> = _uiState.asStateFlow()
    
    init {
        /*loadAllPhotos() don't use paging*/
    }

    fun loadAllPhotos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val photos = repository.getAllPhotos()
                _uiState.value = _uiState.value.copy(
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

    fun onPermissionGranted() {
        _permissionGrantedKey.value += 1
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
