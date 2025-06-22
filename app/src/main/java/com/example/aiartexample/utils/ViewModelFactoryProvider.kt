package com.example.aiartexample.utils

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.aiartexample.ui.home.AiArtStyleViewModel
import com.example.aiartexample.ui.home.AiArtViewModel
import com.example.aiartservice.ServiceFactory
import com.example.pickphoto.utils.ViewModelPickPhotoFactory

object ViewModelFactoryProvider {
    fun provideAiStyleViewModelFactory(): ViewModelProvider.Factory {
        return AiArtStyleViewModel.AiStyleViewModelFactory(ServiceFactory.getService(), ServiceFactory.getService())
    }

    fun provideViewModelPickPhotoFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelPickPhotoFactory(context)
    }
}
