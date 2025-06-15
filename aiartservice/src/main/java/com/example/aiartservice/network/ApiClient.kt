package com.example.aiartservice.network

import com.example.aiartservice.BuildConfig.URL_AI_ART
import com.example.aiartservice.BuildConfig.URL_TIME_STAMP
import com.example.aiartservice.network.interceptor.SignatureInterceptor
import com.example.aiartservice.network.interceptor.createLoggingInterceptor
import com.example.aiartservice.network.request.ArtServiceAI
import com.example.aiartservice.network.request.TimeStampServiceAI
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

internal class ApiClient {
    companion object {
        const val REQUEST_TIMEOUT: Long = 30

        @Volatile
        private var instance: ApiClient? = null

        fun getInstance(): ApiClient {
            return instance ?: synchronized(this) {
                instance ?: ApiClient().also { instance = it }
            }
        }
    }

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(SignatureInterceptor())
            .addInterceptor(createLoggingInterceptor())
            .build()
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun buildArtAi(): ArtServiceAI = buildRetrofit(URL_AI_ART).create()

    fun buildTimeStamp(): TimeStampServiceAI = buildRetrofit(URL_TIME_STAMP).create()
    
    /*fun buildAiStyle(): StyleServiceAI = buildRetrofit(URL_AI_STYLE).create()*/

}