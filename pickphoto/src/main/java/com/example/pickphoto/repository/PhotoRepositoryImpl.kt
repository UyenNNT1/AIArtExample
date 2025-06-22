package com.example.pickphoto.repository

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.example.pickphoto.model.FolderData
import com.example.pickphoto.model.PhotoData
import com.example.pickphoto.utils.MediaUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

class PhotoRepositoryImpl(
    private val contentResolver: ContentResolver,
    private val hasStoragePermission: () -> Boolean
) : PhotoRepository {
    
    override suspend fun getAllPhotos(): List<PhotoData> = withContext(Dispatchers.IO) {
        if (!hasStoragePermission()) {
            return@withContext emptyList()
        }
        
        val photos = mutableListOf<PhotoData>()
        
        try {
            val cursor: Cursor? = contentResolver.query(
                MediaUtils.getPhotosUri(),
                MediaUtils.PHOTO_PROJECTION,
                null,
                null,
                MediaUtils.getPhotosSortOrder()
            )
            
            cursor?.use {
                while (it.moveToNext()) {
                    MediaUtils.cursorToPhotoData(it)?.let { photoData ->
                        photos.add(photoData)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        photos
    }

    override suspend fun downloadPhoto(
        context: Context,
        sourceFile: File,
        fileName: String
    ): Uri? = withContext(Dispatchers.IO) {
        if (!sourceFile.exists() || !sourceFile.canRead()) return@withContext null

        val fileExtension = sourceFile.extension.lowercase()

        val mimeType = when (fileExtension) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "webp" -> "image/webp"
            else -> "image/jpeg"
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/AiArt")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                FileInputStream(sourceFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(uri, contentValues, null, null)
        }

        return@withContext uri
    }


}