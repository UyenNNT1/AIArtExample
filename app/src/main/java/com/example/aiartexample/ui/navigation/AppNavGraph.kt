package com.example.aiartexample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aiartexample.ui.home.AiArtStyleViewModel
import com.example.aiartexample.ui.home.AiArtViewModel
import com.example.aiartexample.ui.home.HomeScreen
import com.example.aiartexample.ui.result.ResultScreen
import com.example.pickphoto.ui.PhotoPickerScreen
import com.example.pickphoto.ui.PhotoPickerViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    aiArtViewModel: AiArtViewModel,
    aiArtStyleViewModel: AiArtStyleViewModel,
    photoPickerViewModel: PhotoPickerViewModel
) {
    NavHost(navController, startDestination = AppRoute.Home.route) {

        composable(AppRoute.Home.route) {
            HomeScreen(
                onOpenPickPhoto = {
                    navController.navigate(AppRoute.PickPhoto.route)
                },
                aiArtViewModel = aiArtViewModel,
                aiStyleViewModel = aiArtStyleViewModel
            )
        }

        composable(AppRoute.PickPhoto.route) {
            PhotoPickerScreen(
                onCloseClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate(AppRoute.Result.route)
                }
            )
        }

        composable(AppRoute.Result.route) {
            ResultScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
