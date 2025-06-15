package com.example.aiartservice.network.repository.aiartv5

import com.example.aiartservice.network.model.AiArtParams
import com.example.aiartservice.network.model.AiArtRequest
import com.example.aiartservice.network.repository.common.HandlerApiWithImageRepo
import com.example.aiartservice.network.request.ArtServiceAI
import com.example.aiartservice.network.response.ResponseState
import com.example.aiartservice.utils.FolderNameConst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class AiArtRepositoryImpl(
    private val artService: ArtServiceAI,
    private val handlerApiWithImageRepo: HandlerApiWithImageRepo
) : AiArtRepository {
    override suspend fun genArtAi(params: AiArtParams): ResponseState<File, Throwable> {
        return withContext(Dispatchers.IO) {
            handlerApiWithImageRepo.callApiWithImage(
                pathImage = params.pathImageOrigin,
                preSignLink = {
                    artService.getLink()
                },
                callApi = { image ->
                    val aiArtRequest = createMultipartBodyAiArt(params, image)
                    val response = artService.genArtAi(aiArtRequest)
                    if (response.isSuccessful) {
                        ResponseState.Success(response.body()?.data?.url)
                    } else {
                        ResponseState.Error(throw Exception(response.message()), response.code())
                    }
                },
                folderName = FolderNameConst.AI_ART
            )
        }
    }

    private fun createMultipartBodyAiArt(
        params: AiArtParams,
        image: String
    ): AiArtRequest {
        return AiArtRequest(
            file = image,
            style = params.style,
            styleId = params.styleId,
            positivePrompt = params.positivePrompt,
            negativePrompt = params.negativePrompt,
            imageSize = params.imageSize,
            fixHeight = params.fixHeight,
            fixWidth = params.fixWidth,
            fixWidthAndHeight = params.fixWidthAndHeight,
            useControlnet = params.useControlnet,
            applyPulid = params.applyPulid,
            seed = params.seed,
            fastMode = params.fastMode
        )
    }

}