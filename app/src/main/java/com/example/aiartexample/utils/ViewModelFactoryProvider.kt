package com.example.aiartexample.utils

import androidx.lifecycle.ViewModelProvider
import com.example.aiartexample.AiArtStyleViewModel
import com.example.aiartexample.AiArtViewModel
import com.example.aiartservice.ServiceFactory

object ViewModelFactoryProvider {
    fun provideAiArtViewModelFactory(): ViewModelProvider.Factory {
        return AiArtViewModel.AiArtViewModelFactory(ServiceFactory.getService())
    }

    fun provideAiStyleViewModelFactory(): ViewModelProvider.Factory {
        return AiArtStyleViewModel.AiStyleViewModelFactory(ServiceFactory.getService())
    }
}
