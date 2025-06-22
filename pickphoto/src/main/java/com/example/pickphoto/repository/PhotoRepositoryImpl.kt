package com.example.pickphoto.repository

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.example.pickphoto.model.FolderData
import com.example.pickphoto.model.PhotoData
import com.example.pickphoto.utils.MediaUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

}