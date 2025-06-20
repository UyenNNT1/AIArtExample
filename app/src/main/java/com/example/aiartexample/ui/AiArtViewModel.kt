package com.example.aiartexample.ui

class AiArtViewModel {
}

data class AiArtUiState(
    var prompt: String = "",
    var imageOriginUrl: String? = null,
    var imageResultUrl: String? = null
)