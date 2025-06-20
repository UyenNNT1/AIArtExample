package com.example.aiartservice.network.repository.aistyle

import com.example.aiartservice.network.model.ApiResponse
import com.example.aiartservice.network.model.CategoryListResponse
import com.example.aiartservice.network.model.CategoryResponse
import com.example.aiartservice.network.request.StyleServiceAI
import com.example.aiartservice.network.response.ResponseState
import com.example.aiartservice.utils.AiServiceLogger
import com.example.aiartservice.utils.AiStyleConstant
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class AiStyleRepositoryImpl(
    private val styleServiceAI: StyleServiceAI
) : AiStyleRepository {

    override suspend fun getAiStyles(segmentValue: String): ResponseState<List<CategoryResponse>, Throwable> {
        return try {
            val response = styleServiceAI.getAiStyles(segmentValue = segmentValue)
            handleResponse(response)
        } catch (e: IOException) {
            logError("IOException", e)
            ResponseState.Error(e, AiStyleConstant.INTERNET_ERROR_CODE)
        } catch (e: HttpException) {
            logError("HttpException", e)
            ResponseState.Error(e, e.code())
        } catch (e: Exception) {
            logError("Exception", e)
            ResponseState.Error(e, AiStyleConstant.UNKNOWN_ERROR_CODE)
        }
    }

    private fun handleResponse(response: Response<ApiResponse<CategoryListResponse>>): ResponseState<List<CategoryResponse>, Throwable> {
        AiServiceLogger.i("AIStyle", "getAiStyles response: $response")
        return if (response.isSuccessful) {
            val items = response.body()?.data?.items.orEmpty()
            AiServiceLogger.i("AIStyle", "getAiStyles body: $items")
            ResponseState.Success(items)
        } else {
            val errorMessage = response.errorBody()?.string() ?: "Unknown error"
            AiServiceLogger.e(
                "AIStyle",
                "getAiStyles response failure with message: $errorMessage"
            )
            ResponseState.Error(Throwable(errorMessage), response.code())
        }
    }

    private fun logError(exceptionType: String, exception: Throwable) {
        AiServiceLogger.e(
            "AIStyle",
            "getAiStyles response failure with $exceptionType: $exception"
        )
    }
}

