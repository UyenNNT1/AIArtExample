package com.example.aiartexample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.aiartexample.utils.ViewModelFactoryProvider
import com.example.aiartservice.AiServiceConfig
import com.example.aiartservice.network.model.AiArtParams
import com.example.core.designsystem.component.AperoTextView
import com.example.core.designsystem.style.LocalCustomTypography
import com.example.pickphoto.PhotoPickerComposeActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val artViewModel: AiArtViewModel by viewModels {
        ViewModelFactoryProvider.provideAiArtViewModelFactory()
    }

    private val artStyleViewModel: AiArtStyleViewModel by viewModels {
        ViewModelFactoryProvider.provideAiStyleViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setUpModuleAiService()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Greeting(
                    name = "Android",
                    modifier = Modifier.padding(innerPadding),
                    onClick = {
                        Log.d("uyenntt", "onCreate: ok")
                        artViewModel.genAiArtImage(AiArtParams(pathImageOrigin = "/storage/sdcard0/Pictures/fe2bbd2d-5a49-4af5-a3b6-3ff7607ec71d.png", style = "6558856da5f7c4e9ba4cf9df"))
                    }
                )
            }
        }
        artStyleViewModel.uiState.onEach {
            Log.d("uyenntt", "category: ${it.categories}")
        }.launchIn(lifecycleScope)
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AperoTextView(
            text = "Hello em $name!",
            textStyle = LocalCustomTypography.current.Headline.semiBold,
            maxLines = 1,
            modifier = Modifier,
            marqueeEnabled = true
        )
        Button(
            onClick = onClick,
            modifier = Modifier
        ) {
            AperoTextView(
                text = "Gen Art",
                textStyle = LocalCustomTypography.current.Body.semiBold,
                maxLines = 1,
                modifier = Modifier,
                marqueeEnabled = false
            )
        }
        Button(
            onClick = {
                val intent = Intent(context, PhotoPickerComposeActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
        ) {
            AperoTextView(
                text = "Open pick photo",
                textStyle = LocalCustomTypography.current.Body.semiBold,
                maxLines = 1,
                modifier = Modifier,
                marqueeEnabled = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}