package com.example.aiartservice.network.interceptor

import com.example.aiartservice.AiServiceConfig
import okhttp3.logging.HttpLoggingInterceptor

fun createLoggingInterceptor() : HttpLoggingInterceptor {
    val logLevel = if (AiServiceConfig.isDebugMode) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }
    return HttpLoggingInterceptor().apply { level = logLevel }
}