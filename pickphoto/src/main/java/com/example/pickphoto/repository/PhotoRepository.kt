package com.example.pickphoto.repository

import com.example.pickphoto.model.FolderData
import com.example.pickphoto.model.PhotoData

interface PhotoRepository {
    
    suspend fun getAllPhotos(): List<PhotoData>
    
    suspend fun getPhotosByFolder(bucketId: String): List<PhotoData>
    
    suspend fun getAllFolders(): List<FolderData>
}
