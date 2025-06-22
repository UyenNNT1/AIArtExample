package com.example.aiartexample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aiartexample.ui.home.AiArtStyleViewModel
import com.example.aiartexample.ui.home.HomeScreen
import com.example.aiartexample.ui.result.ResultScreen
import com.example.aiartexample.ui.pickphoto.PhotoPickerScreen
import com.example.aiartexample.ui.pickphoto.PhotoPickerViewModel
import com.example.aiartexample.ui.result.ResultViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    aiArtStyleViewModel: AiArtStyleViewModel,
    photoPickerViewModel: PhotoPickerViewModel,
    resultViewModel: ResultViewModel
) {
    NavHost(navController, startDestination = AppRoute.Home.route) {

        composable(AppRoute.Home.route) {
            HomeScreen(
                onOpenPickPhoto = {
                    navController.navigate(AppRoute.PickPhoto.route)
                },
                aiStyleViewModel = aiArtStyleViewModel,
                onOpenResultScreen = {
                    navController.navigate(AppRoute.Result.route)
                    resultViewModel.updateImageResult(it)
                }
            )
        }

        composable(AppRoute.PickPhoto.route) {
            PhotoPickerScreen(
                onCloseClick = {
                    navController.popBackStack()
                },
                onNextClick = { photoData ->
                    aiArtStyleViewModel.updateOriginalImage(photoData.path)
                    navController.popBackStack()
                },
                pickPhotoViewModel = photoPickerViewModel
            )
        }

        composable(AppRoute.Result.route) {
            ResultScreen(
                onBackClick = {
                    navController.navigate(AppRoute.Home.route)
                },
                resultViewModel = resultViewModel
            )
        }
    }
}
