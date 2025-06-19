package com.example.pickphoto.utils

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.example.pickphoto.model.PhotoData

object MediaUtils {
    
    val PHOTO_PROJECTION = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.WIDTH,
        MediaStore.Images.Media.HEIGHT
    )
    
    val FOLDER_PROJECTION = arrayOf(
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        "COUNT(*) as photo_count"
    )
    
    fun cursorToPhotoData(cursor: Cursor): PhotoData? {
        return try {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            
            val id = cursor.getLong(idColumn)
            val path = cursor.getString(dataColumn) ?: ""
            val displayName = cursor.getString(displayNameColumn) ?: ""
            
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )
            
            PhotoData(
                id = id.toString(),
                uri = contentUri.toString(),
                path = path
            )
        } catch (e: Exception) {
            null
        }
    }
    
    fun getPhotosUri(): Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    
    fun getPhotosSortOrder(): String = "${MediaStore.Images.Media.DATE_ADDED} DESC"
}