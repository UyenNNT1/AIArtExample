package com.example.pickphoto

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pickphoto.model.PhotoData
import com.example.pickphoto.ui.PhotoPickerScreen

class PhotoPickerComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PhotoPickerApp()
                }
            }
        }
    }
}

@Composable
fun PhotoPickerApp() {
    var selectedPhoto by remember { mutableStateOf<PhotoData?>(null) }

    val samplePhotos = remember {
        List(12) { index ->
            PhotoData(
                id = "photo_$index",
                uri = "",
                path = ""
            )
        }
    }

    PhotoPickerScreen(
        photos = samplePhotos,
        selectedPhoto = selectedPhoto,
        onCloseClick = {
            Log.d("uyenntt", "PhotoPickerApp: close")
        },
        onNextClick = {
            Log.d("uyenntt", "PhotoPickerApp: next")
        },
        onPhotoClick = { photo ->
            selectedPhoto = if (selectedPhoto?.id == photo.id) {
                null
            } else {
                photo
            }
        }
    )
}

@Preview
@Composable
private fun PhotoPickerAppPreview() {
    PhotoPickerApp()
}