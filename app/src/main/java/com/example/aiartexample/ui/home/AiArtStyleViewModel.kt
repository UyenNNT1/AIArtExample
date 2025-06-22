package com.example.aiartexample.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aiartexample.mapper.toModel
import com.example.aiartexample.model.AiArtCategory
import com.example.aiartexample.model.AiArtStyle
import com.example.aiartservice.network.repository.aiartv5.AiArtRepository
import com.example.aiartservice.network.repository.aistyle.AiStyleRepository
import com.example.aiartservice.network.response.ResponseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AiArtStyleViewModel(
    private val aiStyleRepository: AiStyleRepository,
    private val aiGenRepository: AiArtRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AiArtStyleUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchAiArtCategories()
    }

    private fun fetchAiArtCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(categoriesState = UiState.Loading) }
            when (val result = aiStyleRepository.getAiStyles("IN")) {
                is ResponseState.Success -> {
                    val data = result.data
                    val categories = data?.map { categoryResponse -> categoryResponse.toModel() }.orEmpty()
                    _uiState.update {
                        it.copy(
                            categoriesState = UiState.Success(categories),
                            currentCategoryIndex = 0,
                            currentStyleIndex = -1
                        )
                    }
                }

                else -> UiState.Error("Failed to load data")
            }
        }
    }

    fun updateCategoryIndex(index: Int) {
        _uiState.update {
            it.copy(currentCategoryIndex = index, currentStyleIndex = -1)
        }
    }

    fun updateStyleIndex(index: Int) {
        _uiState.update {
            it.copy(currentStyleIndex = index)
        }
    }

    class AiStyleViewModelFactory(
        private val aiStyleRepository: AiStyleRepository,
        private val aiGenRepository: AiArtRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AiArtStyleViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AiArtStyleViewModel(aiStyleRepository, aiGenRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

data class AiArtStyleUiState(
    val categoriesState: UiState<List<AiArtCategory>> = UiState.Loading,
    val currentCategoryIndex: Int = 0,
    val currentStyleIndex: Int = 0,
    val originalImage: String? = null,
    val generatedImage: String? = null
)

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String?) : UiState<Nothing>()
}

/*fake data*/
val fakeAiArtCategories = listOf(
    AiArtCategory(
        id = "cat_0",
        name = "Portrait",
        aiArtStyles = listOf(
            AiArtStyle("style_0_0", "Classic", imageUrl),
            AiArtStyle("style_0_1", "Fantasy", imageUrl),
            AiArtStyle("style_0_2", "Elegant", imageUrl),
            AiArtStyle("style_0_3", "Retro", imageUrl),
            AiArtStyle("style_0_4", "Cyberpunk", imageUrl)
        )
    ),
    AiArtCategory(
        id = "cat_1",
        name = "Anime",
        aiArtStyles = listOf(
            AiArtStyle("style_1_0", "Shonen", imageUrl),
            AiArtStyle("style_1_1", "Shojo", imageUrl),
            AiArtStyle("style_1_2", "Chibi", imageUrl),
            AiArtStyle("style_1_3", "Mecha", imageUrl),
            AiArtStyle("style_1_4", "Ghibli", imageUrl)
        )
    ),
    AiArtCategory(
        id = "cat_2",
        name = "Digital Art",
        aiArtStyles = listOf(
            AiArtStyle("style_2_0", "Neon", imageUrl),
            AiArtStyle("style_2_1", "Sci-Fi", imageUrl),
            AiArtStyle("style_2_2", "Matrix", imageUrl),
            AiArtStyle("style_2_3", "Hacker", imageUrl),
            AiArtStyle("style_2_4", "Digital Smoke", imageUrl)
        )
    ),
    AiArtCategory(
        id = "cat_3",
        name = "Painting",
        aiArtStyles = listOf(
            AiArtStyle("style_3_0", "Oil", imageUrl),
            AiArtStyle("style_3_1", "Watercolor", imageUrl),
            AiArtStyle("style_3_2", "Sketch", imageUrl),
            AiArtStyle("style_3_3", "Impressionism", imageUrl),
            AiArtStyle("style_3_4", "Minimalist", imageUrl)
        )
    ),
    AiArtCategory(
        id = "cat_4",
        name = "Cartoon",
        aiArtStyles = listOf(
            AiArtStyle("style_4_0", "Disney", imageUrl),
            AiArtStyle("style_4_1", "Pixar", imageUrl),
            AiArtStyle("style_4_2", "Comic", imageUrl),
            AiArtStyle("style_4_3", "Toon Shading", imageUrl),
            AiArtStyle("style_4_4", "Flat Color", imageUrl)
        )
    ),
    AiArtCategory(
        id = "cat_5",
        name = "3D Style",
        aiArtStyles = listOf(
            AiArtStyle("style_5_0", "3D Anime", imageUrl),
            AiArtStyle("style_5_1", "Render", imageUrl),
            AiArtStyle("style_5_2", "Voxel", imageUrl),
            AiArtStyle("style_5_3", "Clay", imageUrl),
            AiArtStyle("style_5_4", "Plastic", imageUrl)
        )
    ),
    AiArtCategory(
        id = "cat_6",
        name = "Vintage",
        aiArtStyles = listOf(
            AiArtStyle("style_6_0", "Sepia", imageUrl),
            AiArtStyle("style_6_1", "Monochrome", imageUrl),
            AiArtStyle("style_6_2", "Film Grain", imageUrl),
            AiArtStyle("style_6_3", "Retro VHS", imageUrl),
            AiArtStyle("style_6_4", "Old Print", imageUrl)
        )
    ),
    AiArtCategory(
        id = "cat_7",
        name = "Fantasy",
        aiArtStyles = listOf(
            AiArtStyle("style_7_0", "Fairy", imageUrl),
            AiArtStyle("style_7_1", "Elf", imageUrl),
            AiArtStyle("style_7_2", "Dragon", imageUrl),
            AiArtStyle("style_7_3", "Magic", imageUrl),
            AiArtStyle("style_7_4", "Knight", imageUrl)
        )
    ),
    AiArtCategory(
        id = "cat_8",
        name = "Abstract",
        aiArtStyles = listOf(
            AiArtStyle("style_8_0", "Noise", imageUrl),
            AiArtStyle("style_8_1", "Color Splash", imageUrl),
            AiArtStyle("style_8_2", "Shapes", imageUrl),
            AiArtStyle("style_8_3", "Lines", imageUrl),
            AiArtStyle("style_8_4", "Surreal", imageUrl)
        )
    ),
    AiArtCategory(
        id = "cat_9",
        name = "Realistic",
        aiArtStyles = listOf(
            AiArtStyle("style_9_0", "Photoreal", imageUrl),
            AiArtStyle("style_9_1", "Cinematic", imageUrl),
            AiArtStyle("style_9_2", "HDR", imageUrl),
            AiArtStyle("style_9_3", "Depth", imageUrl),
            AiArtStyle("style_9_4", "Sharp Detail", imageUrl)
        )
    )
)

private const val imageUrl =
    "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/Neon_City_After.jpg"


