package com.dennytech.resamopro.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dennytech.resamopro.ui.screen.auth.login.LoginFragment
import timber.log.Timber

//fun NavGraphBuilder.onBoardingNavGraph(navController: NavHostController) {
//    navigation(
//        route = Graph.ONBOARDING,
//        startDestination = OnBoardingScreen.OnBoardingRoot.route
//    ) {
//        composable(route = OnBoardingScreen.OnBoardingRoot.route) {
//            OnBoardingRootScreen(
//                navigateToOnBoardingOne = {
//                    navController.navigate(OnBoardingScreen.OnBoardingOne.route)
//                },
//                navigateToAuth = {
//                    navController.popBackStack()
//                    navController.navigate(Graph.AUTH)
////                    navController.navigate(Graph.AUTH) {
////                        popUpTo(Graph.ONBOARDING) {
////                            inclusive = true
////                        }
////                    }
//                }
//            )
//        }
//    }
//}

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTH,
        startDestination = AuthScreen.Login.route
    ) {
        composable(AuthScreen.Login.route) {
            LoginFragment(
                navigateToHome = {
                    navController.popBackStack()
                    navController.navigate(Graph.MAIN)

//                    navController.navigate(Graph.MAIN) {
//                        popUpTo(Graph.AUTH) {
//                            inclusive = true
//                        }
//                    }
                },
            )
        }
    }
}

fun NavController.navigateToMainScreen(rootScreen: MainScreen) {
    Timber.d("Selected route: %s", rootScreen.route)
    navigate(rootScreen.route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}