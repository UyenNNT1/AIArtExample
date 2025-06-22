package com.example.aiartexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.aiartexample.ui.home.AiArtStyleViewModel
import com.example.aiartexample.ui.navigation.AppNavGraph
import com.example.aiartexample.utils.ViewModelFactoryProvider
import com.example.aiartservice.AiServiceConfig
import com.example.core.designsystem.style.AppTheme
import com.example.core.designsystem.style.pxToDp
import com.example.aiartexample.ui.pickphoto.PhotoPickerViewModel
import com.example.aiartexample.ui.result.ResultViewModel

class MainActivity : ComponentActivity() {
    private val artStyleViewModel: AiArtStyleViewModel by viewModels {
        ViewModelFactoryProvider.provideAiStyleViewModelFactory()
    }

    private val pickPhotoViewModel: PhotoPickerViewModel by viewModels {
        ViewModelFactoryProvider.provideViewModelPickPhotoFactory(this)
    }

    private val resultViewModel: ResultViewModel by viewModels {
        ViewModelFactoryProvider.provideResultViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setUpModuleAiService()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize().padding(top = 16.pxToDp()),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph(
                        aiArtStyleViewModel = artStyleViewModel,
                        photoPickerViewModel = pickPhotoViewModel,
                        resultViewModel = resultViewModel
                    )
                }
            }
        }
    }

    private fun setUpModuleAiService() {
        AiServiceConfig.setUpData(
            applicationId = "com.wavy.photoeditor.aiart.artgenerator",
            projectName = "com.apero.artimindchatbox",
            apiKey = "sk-HVPPddbYYpKxStQVunC0K8hdCK13GTnIN5Nsj6X55zIOY5PH9b",
            application = application
        )

        AiServiceConfig.setupIntegrity(
            application = application,
            cloudProjectNumber = 1004234323806,
            requestHash = "Glorci",
            urlIntegrity = "https://image-generator.dev.apero.vn/"
        )
    }
}