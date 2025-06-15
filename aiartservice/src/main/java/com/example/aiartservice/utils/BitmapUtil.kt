package com.example.aiartservice.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.aiartservice.AiServiceConfig
import com.example.aiartservice.utils.FileHelper.RESOLUTION_IMAGE_OUTPUT
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.max
import kotlin.math.min

suspend fun String.loadBitmapWithGlide(): Bitmap =
    suspendCoroutine { continuation ->
        Glide.with(AiServiceConfig.application)
            .asBitmap()
            .load(this)
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?,
                ) {
                    return continuation.resume(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    /* no-op */
                }
            })
    }

suspend fun String.loadBitmapAndScaleWithGlide(imageWidth: Int, imageHeight: Int): Bitmap =
    suspendCoroutine { continuation ->
        val size =
            (RESOLUTION_IMAGE_OUTPUT.toDouble() / max(imageWidth, imageHeight)) * min(imageWidth, imageHeight)
        Glide.with(AiServiceConfig.application)
            .asBitmap()
            .load(this)
            .skipMemoryCache(false)
            .override(size.toInt())
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?,
                ) {
                    return continuation.resume(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    /* no-op */
                }
            })
    }
