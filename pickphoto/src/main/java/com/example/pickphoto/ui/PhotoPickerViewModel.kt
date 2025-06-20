package com.example.pickphoto.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pickphoto.model.FolderData
import com.example.pickphoto.model.PhotoData
import com.example.pickphoto.repository.PhotoRepository
import com.example.pickphoto.repository.PhotoRepositoryImpl
import com.example.pickphoto.utils.PermissionUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PhotoPickerUiState(
    val isLoading: Boolean = false,
    val photos: List<PhotoData> = emptyList(),
    val folders: List<FolderData> = emptyList(),
    val selectedFolderId: String? = null,
    val selectedPhotos: List<PhotoData> = emptyList(),
    val hasPermission: Boolean = false,
    val error: String? = null,
    val showFolders: Boolean = false
)

class PhotoPickerViewModel(
    private val context: Context,
    private val repository: PhotoRepository = PhotoRepositoryImpl(context)
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PhotoPickerUiState())
    val uiState: StateFlow<PhotoPickerUiState> = _uiState.asStateFlow()
    
    init {
        checkPermissionAndLoadPhotos()
    }
    
    fun checkPermissionAndLoadPhotos() {
        val hasPermission = PermissionUtils.hasStoragePermission(context)
        _uiState.value = _uiState.value.copy(hasPermission = hasPermission)
        
        if (hasPermission) {
            loadAllPhotos()
            loadFolders()
        }
    }
    
    fun loadAllPhotos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val photos = repository.getAllPhotos()
                _uiState.value = _uiState.value.copy(
                    photos = photos,
                    isLoading = false,
                    selectedFolderId = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load photos: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
    
    fun loadPhotosByFolder(folderId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val photos = repository.getPhotosByFolder(folderId)
                _uiState.value = _uiState.value.copy(
                    photos = photos,
                    isLoading = false,
                    selectedFolderId = folderId,
                    showFolders = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load photos from folder: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
    
    fun loadFolders() {
        viewModelScope.launch {
            try {
                val folders = repository.getAllFolders()
                _uiState.value = _uiState.value.copy(folders = folders)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load folders: ${e.message}"
                )
            }
        }
    }
    
    fun togglePhotoSelection(photo: PhotoData) {
        val currentSelected = _uiState.value.selectedPhotos
        val newSelected = if (currentSelected.contains(photo)) {
            currentSelected - photo
        } else {
            currentSelected + photo
        }
        
        _uiState.value = _uiState.value.copy(selectedPhotos = newSelected)
    }
    
    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedPhotos = emptyList())
    }
    
    fun selectAllPhotos() {
        _uiState.value = _uiState.value.copy(selectedPhotos = _uiState.value.photos)
    }
    
    fun toggleFoldersView() {
        _uiState.value = _uiState.value.copy(showFolders = !_uiState.value.showFolders)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun getRequiredPermission(): String {
        return PermissionUtils.getRequiredStoragePermission()
    }
}
