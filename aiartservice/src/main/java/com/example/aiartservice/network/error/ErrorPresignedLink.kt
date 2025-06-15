package com.example.aiartservice.network.error

data class ErrorPresignedLink(
    override val cause: Throwable? = null,
    override val message: String? = null,
    val code: Int? = null
) : Throwable(cause = cause, message = message)