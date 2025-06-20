package com.example.aiartservice.network.model

import com.google.gson.annotations.SerializedName

data class CategoryListResponse(
    @SerializedName("items") val items: List<CategoryResponse>
)