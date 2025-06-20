package com.example.aiartservice.network.model

import com.google.gson.annotations.SerializedName

data class StyleResponse(
    @SerializedName("_id") val id: String = "",
    @SerializedName("key") val key: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("priority") val priority: Double = 0.0,
)