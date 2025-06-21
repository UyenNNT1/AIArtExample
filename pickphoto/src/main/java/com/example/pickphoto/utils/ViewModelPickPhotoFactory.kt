package com.example.pickphoto.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pickphoto.ui.PhotoPickerViewModel

class ViewModelPickPhotoFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoPickerViewModel::class.java)) {
            val repository = PhotoRepositoryFactory.create(context)
            return PhotoPickerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 