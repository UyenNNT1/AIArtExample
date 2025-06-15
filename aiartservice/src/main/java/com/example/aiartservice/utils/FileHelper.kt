package com.example.aiartservice.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.annotation.Keep
import androidx.exifinterface.media.ExifInterface
import com.example.aiartservice.AiServiceConfig
import com.example.aiartservice.network.response.ResponseState
import com.example.aiartservice.utils.ServiceError.CODE_FILE_NULL
import com.example.aiartservice.utils.ServiceError.SAVE_FILE_ERROR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import java.util.UUID
import kotlin.math.max

@Keep
object FileHelper {
    const val RESOLUTION_IMAGE_OUTPUT = 1600

    fun getFolderInCache(folderName: String): String {
        val folder = File(AiServiceConfig.application.cacheDir.path, folderName)
        if (folder.exists().not()) {
            folder.mkdirs()
        }
        return folder.path
    }

    private suspend fun saveFileFromResponseBody(
        body: ResponseBody,
        fileResult: File,
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            body.byteStream().use { inputStream ->
                FileOutputStream(fileResult.path).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
            }
            Result.success(true)
        } catch (e: Exception) {
            deleteFileInCache(fileResult.path)
            Result.failure(e)
        }
    }

    suspend fun deleteFileInCache(pathFile: String) {
        val file = File(pathFile)
        if (file.exists()) {
            withContext(Dispatchers.IO) {
                file.delete()
            }
        }
    }

    suspend fun String.preProcessingPath(folderName: String): String {
        AiServiceLogger.i("preProcessingPath", "file path: $this, folderName: $folderName")
        val bitmapOption = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(this@preProcessingPath, bitmapOption)
        val maxResolution = max(bitmapOption.outWidth, bitmapOption.outHeight)
        val isBigSize = maxResolution > RESOLUTION_IMAGE_OUTPUT
        val rotation = this.getRotationValue()
        val isImageJPG = File(this).extension.lowercase().let {
            it == "jpg" || it == "jpeg"
        }
        return when {
            isBigSize -> {
                val bitmapResult =
                    this.loadBitmapAndScaleWithGlide(bitmapOption.outWidth, bitmapOption.outHeight)
                saveBitmapToFile(bitmapResult, folderName, System.currentTimeMillis().toString())
            }

            rotation != 0 || isImageJPG.not() -> {
                val bitmapInput = this.loadBitmapWithGlide()
                saveBitmapToFile(bitmapInput, folderName, System.currentTimeMillis().toString())
            }

            else -> this
        }
    }

    suspend fun String.getRotationValue(): Int = withContext(Dispatchers.Default) {
        val exifInterface = ExifInterface(this@getRotationValue)
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
        )
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    private suspend fun saveBitmapToFile(
        bitmap: Bitmap,
        folderName: String,
        fileName: String,
    ): String =
        withContext(Dispatchers.IO) {
            val newFile = File(getFolderInCache(folderName), "image_$fileName.jpg").apply {
                outputStream().use { out ->
                    bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        out
                    )
                }
            }
            return@withContext newFile.absolutePath
        }

    suspend fun downloadAndSaveFile(
        url: String,
        nameFolder: String
    ): ResponseState<File, Throwable> = withContext(Dispatchers.IO) {
        try {
            val folderPath = getFolderInCache(nameFolder)
            val fileName = "${UUID.randomUUID()}.png"
            val outputFile = File(folderPath, fileName)

            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()
            Log.d("downloadAndSaveFile", "url: $url")

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext ResponseState.Error(
                        Throwable("Failed to download file: ${response.code}"),
                        response.code
                    )
                }

                response.body?.let { body ->
                    val saveResult = saveFileFromResponseBody(body, outputFile)
                    return@withContext saveResult.fold(
                        onSuccess = {
                            ResponseState.Success(outputFile)
                        },
                        onFailure = {
                            ResponseState.Error(Throwable(SAVE_FILE_ERROR), CODE_FILE_NULL)
                        }
                    )
                } ?: run {
                    return@withContext ResponseState.Error(
                        Throwable(SAVE_FILE_ERROR),
                        CODE_FILE_NULL
                    )
                }
            }
        } catch (e: Exception) {
            ResponseState.Error(e, CODE_FILE_NULL)
        }
    }
}
