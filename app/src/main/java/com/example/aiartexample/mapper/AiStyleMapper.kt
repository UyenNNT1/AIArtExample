package com.example.aiartexample.mapper

import com.example.aiartexample.model.AiArtCategory
import com.example.aiartexample.model.AiArtStyle
import com.example.aiartservice.network.model.CategoryResponse
import com.example.aiartservice.network.model.StyleResponse


fun CategoryResponse.toModel(): AiArtCategory {
    return AiArtCategory(id = this.id, name = this.name, aiArtStyles = this.styles.map { it.toModel() })
}

fun StyleResponse.toModel(): AiArtStyle {
    return AiArtStyle(id = id, name = name, thumbnail = key)
}