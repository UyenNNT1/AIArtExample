package com.example.aiartservice.network.repository.timestamp

interface TimeStampRepository  {
    suspend fun getTimeStampFromServer(): Result<Long>
}