package com.example.pickphoto.repository

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.example.pickphoto.model.PhotoData
import com.example.pickphoto.utils.MediaUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

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

    inner class PhotoPagingSource : PagingSource<Int, PhotoData>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoData> {
            val page = params.key ?: 0
            val pageSize = params.loadSize
            if (!hasStoragePermission()) {
                return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
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
                    val start = page * pageSize
                    if (start < 0) return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
                    if (it.moveToPosition(start)) {
                        var count = 0
                        do {
                            MediaUtils.cursorToPhotoData(it)?.let { photoData ->
                                photos.add(photoData)
                            }
                            count++
                        } while (count < pageSize && it.moveToNext())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return LoadResult.Error(e)
            }
            val nextKey = if (photos.size < pageSize) null else page + 1
            val prevKey = if (page == 0) null else page - 1
            return LoadResult.Page(photos, prevKey = prevKey, nextKey = nextKey)
        }
        override fun getRefreshKey(state: PagingState<Int, PhotoData>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }
    }

    override fun getPhotoPagingSource(): PagingSource<Int, PhotoData> {
        return PhotoPagingSource()
    }

    override fun getPhotoPagingFlow(pageSize: Int): Flow<PagingData<PhotoData>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
            pagingSourceFactory = { getPhotoPagingSource() }
        ).flow
    }
}