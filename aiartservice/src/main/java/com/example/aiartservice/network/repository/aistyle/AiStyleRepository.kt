package com.example.aiartservice.network.repository.aistyle

import com.example.aiartservice.network.model.CategoryResponse
import com.example.aiartservice.network.response.ResponseState
import com.example.aiartservice.utils.AiStyleConstant.SEGMENT_DEFAULT

interface AiStyleRepository {
    suspend fun getAiStyles(segmentValue: String = SEGMENT_DEFAULT): ResponseState<List<CategoryResponse>, Throwable>
}