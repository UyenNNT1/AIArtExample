package com.example.pickphoto.repository

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.example.pickphoto.model.FolderData
import com.example.pickphoto.model.PhotoData
import com.example.pickphoto.utils.MediaUtils
import com.example.pickphoto.utils.PermissionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoRepositoryImpl(private val context: Context) : PhotoRepository {
    
    private val contentResolver: ContentResolver = context.contentResolver
    
    override suspend fun getAllPhotos(): List<PhotoData> = withContext(Dispatchers.IO) {
        if (!PermissionUtils.hasStoragePermission(context)) {
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
    
    override suspend fun getPhotosByFolder(bucketId: String): List<PhotoData> = withContext(Dispatchers.IO) {
        if (!PermissionUtils.hasStoragePermission(context)) {
            return@withContext emptyList()
        }
        
        val photos = mutableListOf<PhotoData>()
        
        try {
            val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
            val selectionArgs = arrayOf(bucketId)
            
            val cursor: Cursor? = contentResolver.query(
                MediaUtils.getPhotosUri(),
                MediaUtils.PHOTO_PROJECTION,
                selection,
                selectionArgs,
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
    
    override suspend fun getAllFolders(): List<FolderData> = withContext(Dispatchers.IO) {
        if (!PermissionUtils.hasStoragePermission(context)) {
            return@withContext emptyList()
        }
        
        val folders = mutableListOf<FolderData>()
        
        try {
            val projection = arrayOf(
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media._ID
            )
            
            val cursor: Cursor? = contentResolver.query(
                MediaUtils.getPhotosUri(),
                projection,
                null,
                null,
                "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} ASC"
            )
            
            val folderMap = mutableMapOf<String, Pair<String, String>>()
            
            cursor?.use {
                while (it.moveToNext()) {
                    val bucketIdColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
                    val bucketNameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                    val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    
                    val bucketId = it.getString(bucketIdColumn) ?: continue
                    val bucketName = it.getString(bucketNameColumn) ?: "Unknown"
                    val photoId = it.getLong(idColumn)
                    
                    if (!folderMap.containsKey(bucketId)) {
                        val thumbnailUri = android.content.ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            photoId
                        ).toString()
                        folderMap[bucketId] = Pair(bucketName, thumbnailUri)
                    }
                }
            }
            
            // Count photos in each folder
            folderMap.forEach { (bucketId, folderInfo) ->
                val photoCount = getPhotoCountInFolder(bucketId)
                folders.add(
                    FolderData(
                        id = bucketId,
                        name = folderInfo.first,
                        photoCount = photoCount,
                        thumbnailUri = folderInfo.second
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        folders
    }
    
    private suspend fun getPhotoCountInFolder(bucketId: String): Int = withContext(Dispatchers.IO) {
        try {
            val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
            val selectionArgs = arrayOf(bucketId)
            
            val cursor: Cursor? = contentResolver.query(
                MediaUtils.getPhotosUri(),
                arrayOf(MediaStore.Images.Media._ID),
                selection,
                selectionArgs,
                null
            )
            
            cursor?.use {
                return@withContext it.count
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        0
    }
}