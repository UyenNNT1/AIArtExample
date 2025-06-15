package com.example.aiartservice.network.response

sealed class ResponseState<out T : Any, out E : Any> {
    data class Success<T : Any>(val data: T?) : ResponseState<T, Nothing>()
    data class Error<E : Any>(val error: E, val code: Int) : ResponseState<Nothing, E>()
}