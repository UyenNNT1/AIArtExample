package com.example.aiartexample.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AiArtStyle(val id: String, val name: String, val thumbnail: String): Parcelable