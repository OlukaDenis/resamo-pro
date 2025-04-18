package com.dennytech.resamopro.ui.navigation
object Graph {
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val MAIN = "main_graph"
}

sealed class AuthScreen(val route: String) {
    data object Login : AuthScreen("login")
    data object Intro : AuthScreen("intro")
    data object NewStore : AuthScreen("auth_new_store")
}

sealed class MainScreen(val route: String) {
    data object Home : MainScreen("home")
    data object NewProduct : MainScreen("new_product")
    data object UpdateProduct : MainScreen("update_product/{product}")
    data object ProductDetail : MainScreen("detail/{product}")
    data object Account : MainScreen("account")
    data object Products : MainScreen("products")
    data object Users : MainScreen("users")
    data object Stores : MainScreen("stores")
    data object StoreDetail : MainScreen("stores/{storeId}")
    data object Sales : MainScreen("sales")
    data object NewUser : MainScreen("new_user")
    data object NewStore : MainScreen("new_store")
    data object Counts : MainScreen("counts")
}