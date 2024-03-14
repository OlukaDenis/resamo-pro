package com.dennytech.resamopro.ui.navigation
object Graph {
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val ONBOARDING = "onboarding_graph"
    const val MAIN = "main_graph"
}

sealed class OnBoardingScreen(val route: String) {
    data object OnBoardingRoot : OnBoardingScreen("onboarding_root")
    data object OnBoardingOne : OnBoardingScreen("onboarding_1")
    data object OnBoardingTwo : OnBoardingScreen("onboarding_2")
    data object OnBoardingThree : OnBoardingScreen("onboarding_3")
}

sealed class AuthScreen(val route: String) {
    data object Login : AuthScreen("login")
    data object Intro : AuthScreen("intro")
    data object Otp : AuthScreen("otp")
    data object Register : AuthScreen("register")
}

sealed class MainScreen(val route: String) {
    data object Home : MainScreen("home")
    data object Notification : MainScreen("notifications")
    data object Account : MainScreen("account")
    data object Products : MainScreen("products")
}