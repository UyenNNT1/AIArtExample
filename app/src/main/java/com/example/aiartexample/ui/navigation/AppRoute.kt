package com.example.aiartexample.ui.navigation

sealed class AppRoute(val route: String) {
    data object Home : AppRoute("home")
    data object PickPhoto : AppRoute("pick_photo")
    data object Result : AppRoute("result")
}
