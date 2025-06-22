package com.example.pickphoto.repository

import android.content.Context
import android.net.Uri
import com.example.pickphoto.model.FolderData
import com.example.pickphoto.model.PhotoData
import java.io.File

interface PhotoRepository {
    
    suspend fun getAllPhotos(): List<PhotoData>

    suspend fun downloadPhoto(context: Context, sourceFile: File, fileName: String) : Uri?
}
