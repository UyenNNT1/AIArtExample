package com.example.aiartexample.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aiartexample.model.ResultError
import com.example.aiartservice.network.model.AiArtParams
import com.example.aiartservice.network.repository.aiartv5.AiArtRepository
import com.example.aiartservice.network.response.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AiArtViewModel(
    private val aiServiceRepository: AiArtRepository
) : ViewModel() {
    private val _resultSuccess = MutableStateFlow<String?>(null)
    val resultSuccess = _resultSuccess.asStateFlow()
    private val _resultError = MutableStateFlow<ResultError?>(null)
    val resultError = _resultError.asStateFlow()


    fun genAiArtImage(params: AiArtParams) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val res = aiServiceRepository.genArtAi(params)) {
                is ResponseState.Success -> {
                    _resultSuccess.value = res.data?.path
                }

                is ResponseState.Error -> {
                    _resultError.value = ResultError(res.code, res.error.message!!)
                }
            }
        }
    }

    class AiArtViewModelFactory(
        private val aiArtRepository: AiArtRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AiArtViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AiArtViewModel(aiArtRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}