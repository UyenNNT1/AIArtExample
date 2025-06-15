package com.example.aiartservice.network.repository.common


import com.example.aiartservice.network.model.PresignedLink
import com.example.aiartservice.network.response.ResponseState
import retrofit2.Response
import java.io.File

internal interface HandlerApiWithImageRepo {
    suspend fun callApiWithImage(
        pathImage: String,
        preSignLink: suspend () -> Response<PresignedLink>,
        callApi: suspend (String) -> ResponseState<String, Throwable>,
        folderName: String,
    ): ResponseState<File, Throwable>

    suspend fun pushImageToServer(
        url: String,
        file: String,
        folderName: String
    ): ResponseState<String, Throwable>
}