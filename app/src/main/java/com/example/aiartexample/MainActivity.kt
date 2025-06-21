package com.example.aiartexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.aiartexample.ui.navigation.AppNavGraph
import com.example.aiartservice.AiServiceConfig
import com.example.core.designsystem.style.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setUpModuleAiService()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph()
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