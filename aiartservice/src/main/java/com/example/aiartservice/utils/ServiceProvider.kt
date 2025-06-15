package com.example.aiartservice.utils

import com.example.aiartservice.network.ApiClient
import com.example.aiartservice.network.repository.aiartv5.AiArtRepository
import com.example.aiartservice.network.repository.aiartv5.AiArtRepositoryImpl
import com.example.aiartservice.network.repository.common.HandlerApiWithImageImpl
import com.example.aiartservice.network.repository.timestamp.TimeStampRepository
import com.example.aiartservice.network.repository.timestamp.TimeStampRepositoryImpl

object ServiceProvider {
    private val apiClient: ApiClient = ApiClient.getInstance()

    fun provideTimeStampRepository(): TimeStampRepository =
        TimeStampRepositoryImpl(apiClient.buildTimeStamp())

    fun provideAiArtRepository(): AiArtRepository =
        AiArtRepositoryImpl(
            apiClient.buildArtAi(),
            HandlerApiWithImageImpl(apiClient.buildArtAi())
        )

}
