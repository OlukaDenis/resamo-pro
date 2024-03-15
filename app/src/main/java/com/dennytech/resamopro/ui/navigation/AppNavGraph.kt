package com.dennytech.resamopro.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.resamopro.ui.screen.auth.login.LoginFragment
import com.dennytech.resamopro.ui.screen.auth.onboarding.OnBoardingRootFragment
import com.dennytech.resamopro.ui.screen.main.home.HomeFragment
import com.dennytech.resamopro.ui.screen.main.products.ProductsFragment
import com.dennytech.resamopro.ui.screen.main.products.create.CreateProductFragment
import com.dennytech.resamopro.ui.screen.main.products.sale.RecordSaleFragment
import com.dennytech.resamopro.ui.screen.main.profile.ProfileFragment
import com.google.gson.GsonBuilder
import timber.log.Timber

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTH,
        startDestination = AuthScreen.Intro.route
    ) {

        composable(AuthScreen.Intro.route) {
            OnBoardingRootFragment(
                navigateToLogin = {
                    navController.navigate(AuthScreen.Login.route)
                },
                navigateToHome = {
//                    navController.popBackStack()
//                    navController.navigate(Graph.MAIN)

                    navController.navigate(Graph.MAIN) {
                        popUpTo(Graph.AUTH) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AuthScreen.Login.route) {
            LoginFragment(
                navigateToHome = {
//                    navController.popBackStack()
//                    navController.navigate(Graph.MAIN)

                    navController.navigate(Graph.MAIN) {
                        popUpTo(Graph.AUTH) {
                            inclusive = true
                        }
                    }
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