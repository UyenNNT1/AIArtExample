package com.example.pickphoto

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.pickphoto.model.PhotoData
import com.example.pickphoto.ui.PhotoPickerScreen
import com.example.pickphoto.ui.PhotoPickerViewModel
import com.example.pickphoto.utils.PermissionUtils

class PhotoPickerComposeActivity : ComponentActivity() {
    private lateinit var viewModel: PhotoPickerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = PhotoPickerViewModel(this)

        val permission = PermissionUtils.getRequiredStoragePermission()
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.loadAllPhotos()
            } else {
                Log.d("uyenntt", "Permission denied")
            }
        }

        if (PermissionUtils.hasStoragePermission(this)) {
            viewModel.loadAllPhotos()
        } else {
            requestPermissionLauncher.launch(permission)
        }

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PhotoPickerApp(photos = uiState.photos)
                }
            }
        }
    }
}

@Composable
fun PhotoPickerApp(photos: List<PhotoData>) {
    val context = LocalContext.current
    var selectedPhoto by remember { mutableStateOf<PhotoData?>(null) }

    PhotoPickerScreen(
        photos = photos,
        selectedPhoto = selectedPhoto,
        onCloseClick = {
            (context as? android.app.Activity)?.finish()
        },
        onNextClick = {
            // Xử lý action next ở đây
        },
        onPhotoClick = { photo ->
            selectedPhoto = if (selectedPhoto?.id == photo.id) null else photo
        }
    )
}

@Preview
@Composable
private fun PhotoPickerAppPreview() {
    PhotoPickerApp(photos = emptyList())
}