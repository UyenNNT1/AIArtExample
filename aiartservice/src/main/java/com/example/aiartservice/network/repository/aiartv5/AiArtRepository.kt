package com.example.aiartservice.network.repository.aiartv5

import com.example.aiartservice.network.model.AiArtParams
import com.example.aiartservice.network.response.ResponseState
import java.io.File

interface AiArtRepository  {
    suspend fun genArtAi(params: AiArtParams): ResponseState<File, Throwable>
}