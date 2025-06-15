package com.example.aiartservice.utils

import android.util.Log

internal object AiServiceLogger {
    @JvmStatic
    fun d(tag: String, message: String) {
        Log.d("AIService_$tag", message)
    }

    @JvmStatic
    fun i(tag: String, message: String) {
        Log.i("AIService_$tag", message)
    }

    @JvmStatic
    fun e(tag: String, message: String) {
        Log.e("AIService_$tag", message)
    }
}