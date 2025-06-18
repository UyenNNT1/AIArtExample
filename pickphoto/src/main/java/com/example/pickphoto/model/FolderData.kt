package com.example.pickphoto.model

data class FolderData(
    val id: String,
    val name: String,
    val photoCount: Int,
    val thumbnailUri: String?
)