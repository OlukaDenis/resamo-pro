package com.dennytech.resamopro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dennytech.resamopro.ui.MainFragment

@Composable
fun ResamoNavHost(navHostController: NavHostController) {
//    val authShareViewModel: AuthShareViewModel = hiltViewModel()

    NavHost(navController = navHostController,
        route = Graph.ROOT,
        startDestination = Graph.AUTH, // TODO make this start from onboarding
    ) {
//        onBoardingNavGraph(navHostController)
        authNavGraph(navController = navHostController)

        composable(Graph.MAIN) {
            MainFragment()
        }
    }
}
