package com.example.pickphoto.utils

import android.content.Context
import com.example.pickphoto.repository.PhotoRepository
import com.example.pickphoto.repository.PhotoRepositoryImpl

object PhotoRepositoryFactory {
    
    fun create(context: Context): PhotoRepository {
        return PhotoRepositoryImpl(
            contentResolver = context.contentResolver,
            hasStoragePermission = { PermissionUtils.hasStoragePermission(context) }
        )
    }
} 