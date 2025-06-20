package com.example.aiartservice.network.request

import com.example.aiartservice.AiServiceConfig
import com.example.aiartservice.network.model.ApiResponse
import com.example.aiartservice.network.model.CategoryListResponse
import com.example.aiartservice.utils.AiStyleConstant.DEFAULT_STYLE_TYPE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StyleServiceAI {
    @GET("category")
    suspend fun getAiStyles(
        @Query("project") appName: String = AiServiceConfig.projectName,
        @Query("styleType") styleType: String = DEFAULT_STYLE_TYPE,
        @Query("isApp") isApp: Boolean = true,
        @Query("segmentValue") segmentValue: String,
    ): Response<ApiResponse<CategoryListResponse>>
}