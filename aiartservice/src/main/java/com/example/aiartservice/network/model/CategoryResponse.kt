package com.example.aiartservice.network.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("description") val description: String = "",
    @SerializedName("_id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("priority") val priority: Double = 0.0,
    @SerializedName("project") val project: String = "",
    @SerializedName("styles") val styles: List<StyleResponse> = emptyList(),
    @SerializedName("subscriptionType") val subscriptionType: String = "",
)