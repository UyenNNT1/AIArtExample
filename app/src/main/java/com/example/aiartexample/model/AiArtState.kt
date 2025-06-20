package com.example.aiartexample.model

data class AiArtUiState(
    var prompt: String = "",
    var imageOriginUrl: String? = null,
    var imageResultUrl: String? = null
)

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Error(val ignoredMessage: String?) : UiState<Nothing>()
    data class Success<out T>(val ignoredData: T) : UiState<T>()
}