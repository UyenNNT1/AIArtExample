package com.example.pickphoto.repository

import android.content.Context
import android.net.Uri
import com.example.pickphoto.model.PhotoData
import java.io.File
import androidx.paging.PagingSource
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    
    suspend fun getAllPhotos(): List<PhotoData>

    suspend fun downloadPhoto(context: Context, sourceFile: File, fileName: String) : Uri?

    fun getPhotoPagingSource(): PagingSource<Int, PhotoData>
    fun getPhotoPagingFlow(pageSize: Int = 50): Flow<PagingData<PhotoData>>
}
