package com.example.aiartservice.network.interceptor

import com.apero.signature.SignatureParser
import com.example.aiartservice.AiServiceConfig
import com.example.aiartservice.BuildConfig
import com.example.aiartservice.utils.ServiceConst.NOT_GET_API_TOKEN
import okhttp3.Interceptor
import okhttp3.Response
import kotlin.collections.iterator

internal class SignatureInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val signatureData = SignatureParser.parseData(
            AiServiceConfig.apiKey,
            BuildConfig.PUBLIC_KEY,
            AiServiceConfig.timeStamp
        )
        val tokenIntegrity = signatureData.tokenIntegrity.ifEmpty { NOT_GET_API_TOKEN }

        val headers = hashMapOf(
            "Accept" to "application/json",
            "Content-Type" to "application/json",
            "device" to "android",
            "x-api-signature" to signatureData.signature,
            "x-api-timestamp" to signatureData.timeStamp.toString(),
            "x-api-keyid" to signatureData.keyId,
            "x-api-token" to tokenIntegrity,
            "x-api-bundleId" to AiServiceConfig.applicationId,
            "App-name" to AiServiceConfig.projectName,
        )
        val requestBuilder = chain.request().newBuilder()
        for ((key, value) in headers) {
            requestBuilder.addHeader(key, value)
        }
        return chain.proceed(requestBuilder.build())
    }
}