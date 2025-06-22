package com.example.aiartexample.utils

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.aiartexample.ui.home.AiArtStyleViewModel
import com.example.aiartexample.ui.pickphoto.PhotoPickerViewModel
import com.example.aiartexample.ui.result.ResultViewModel
import com.example.aiartservice.ServiceFactory

object ViewModelFactoryProvider {
    fun provideAiStyleViewModelFactory(): ViewModelProvider.Factory {
        return AiArtStyleViewModel.AiStyleViewModelFactory(ServiceFactory.getService(), ServiceFactory.getService())
    }

    fun provideViewModelPickPhotoFactory(context: Context): ViewModelProvider.Factory {
        return PhotoPickerViewModel.PickPhotoViewModelFactory(context)
    }

    fun provideResultViewModelFactory(): ViewModelProvider.Factory {
        return ResultViewModel.ResultViewModelFactory()
    }
}
