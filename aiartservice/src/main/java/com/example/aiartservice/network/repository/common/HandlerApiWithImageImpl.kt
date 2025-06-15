package com.example.aiartservice.network.repository.common

import android.util.Log
import com.example.aiartservice.network.error.ErrorPresignedLink
import com.example.aiartservice.network.error.ErrorPushImage
import com.example.aiartservice.network.model.PresignedLink
import com.example.aiartservice.network.request.PushImageService
import com.example.aiartservice.network.response.ResponseState
import com.example.aiartservice.utils.AiServiceLogger
import com.example.aiartservice.utils.FileHelper
import com.example.aiartservice.utils.FileHelper.preProcessingPath
import com.example.aiartservice.utils.ServiceConst.TIME_OUT_DURATION
import com.example.aiartservice.utils.ServiceError.CODE_GET_LINK_ERROR
import com.example.aiartservice.utils.ServiceError.CODE_PUSH_IMAGE_ERROR
import com.example.aiartservice.utils.ServiceError.CODE_TIMEOUT_ERROR
import com.example.aiartservice.utils.ServiceError.CODE_UNKNOWN_ERROR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException

internal class HandlerApiWithImageImpl(
    private val pushImageService: PushImageService
) : HandlerApiWithImageRepo {
    override suspend fun callApiWithImage(
        pathImage: String,
        preSignLink: suspend () -> Response<PresignedLink>,
        callApi: suspend (String) -> ResponseState<String, Throwable>,
        folderName: String
    ):ResponseState<File, Throwable> = withTimeoutOrNull(TIME_OUT_DURATION) {
        withContext(Dispatchers.IO) {
            try {
                val link = preSignLink()
                Log.d("ActivityTest", "$link")
                if (link.isSuccessful) {
                    val body = link.body()
                    val url = body?.data?.url ?: ""
                    val path = body?.data?.path ?: ""
                    val responsePushImage = pushImageToServer(url, pathImage, folderName)
                    if (responsePushImage is ResponseState.Success) {

                        val response = callApi(path)
                        if (response is ResponseState.Success && response.data != null) {
                            return@withContext FileHelper.downloadAndSaveFile(
                                response.data,
                                folderName
                            )
                        } else {
                            return@withContext ResponseState.Error(
                                (response as? ResponseState.Error)?.error ?: Throwable("Unknown error"),
                                CODE_UNKNOWN_ERROR
                            )
                        }
                    } else {
                        AiServiceLogger.e("", "responsePushImage: $responsePushImage")
                        ResponseState.Error(
                            (responsePushImage as? ResponseState.Error)?.error
                                ?: Throwable("Unknown error"),
                            CODE_PUSH_IMAGE_ERROR
                        )
                    }
                } else {
                    AiServiceLogger.e("", "link: ${link.message()}")
                    ResponseState.Error(
                        ErrorPresignedLink(
                            message = link.message().ifEmpty {
                                "get link error ${link.code()}\n ${
                                    link.errorBody()?.string()
                                }"
                            }),
                        CODE_GET_LINK_ERROR
                    )
                }
            } catch (e: SocketTimeoutException) {
                ResponseState.Error(
                    e,
                    CODE_TIMEOUT_ERROR
                )
            } catch (e: Throwable) {
                AiServiceLogger.e("", e.stackTraceToString())
                ResponseState.Error(
                    e,
                    CODE_UNKNOWN_ERROR
                )
            }
        }
    } ?: ResponseState.Error(
        Throwable("timeout or connection issue"),
        CODE_TIMEOUT_ERROR
    )

    override suspend fun pushImageToServer(
        url: String,
        file: String,
        folderName: String
    ): ResponseState<String, Throwable> = withTimeoutOrNull(TIME_OUT_DURATION) {
        withContext(Dispatchers.IO) {
            val inputFile = File(file.preProcessingPath(folderName = folderName))
            when {
                !inputFile.exists() -> {
                    return@withContext ResponseState.Error(
                        IOException("File does not exist"),
                        -1
                    )
                }

                !inputFile.canRead() -> {
                    return@withContext ResponseState.Error(
                        IOException("Cannot read file"),
                        -1
                    )
                }
            }
            val response = pushImageService.pushImageToServer(url, createRequestBody(inputFile))
            Log.d("Clothes_activity", "responsePushImage: $response")
            if (response.isSuccessful) {
                ResponseState.Success(response.body()?.string() ?: "")
            } else {
                ResponseState.Error(
                    ErrorPushImage(
                        message = response.message()
                            .ifEmpty {
                                "push image error ${response.code()} \n ${
                                    response.errorBody()?.string()
                                }"
                            },
                        code = response.code(),
                    ), response.code()
                )

            }
        }
    } ?: ResponseState.Error(
        Throwable("timeout or connection issue"),
        CODE_TIMEOUT_ERROR
    )

    private fun createRequestBody(imageFile: File): RequestBody {
        val mimeType = when (imageFile.extension.lowercase()) {
            "png" -> "image/png"
            "jpg", "jpeg" -> "image/jpeg"
            else -> throw IllegalArgumentException("Only PNG or JPG files are supported")
        }
        return imageFile.asRequestBody(mimeType.toMediaTypeOrNull())
    }
}