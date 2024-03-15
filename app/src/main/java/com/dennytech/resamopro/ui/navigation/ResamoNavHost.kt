package com.dennytech.resamopro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.resamopro.ui.MainFragment
import com.dennytech.resamopro.ui.screen.main.home.HomeFragment
import com.dennytech.resamopro.ui.screen.main.products.ProductsFragment
import com.dennytech.resamopro.ui.screen.main.products.create.CreateProductFragment
import com.dennytech.resamopro.ui.screen.main.products.sale.RecordSaleFragment
import com.dennytech.resamopro.ui.screen.main.profile.ProfileFragment
import com.google.gson.GsonBuilder

@Composable
fun ResamoNavHost(navHostController: NavHostController) {
//    val authShareViewModel: AuthShareViewModel = hiltViewModel()

    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Graph.AUTH,
    ) {
        authNavGraph(navController = navHostController)
//        mainNavGraph(navController = navHostController)


        composable(Graph.MAIN) {
            MainFragment(navController = navHostController)
        }

    }

}
