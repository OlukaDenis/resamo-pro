package com.dennytech.resamopro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dennytech.resamopro.ui.screen.main.home.HomeFragment
import com.dennytech.resamopro.ui.screen.main.products.ProductsFragment
import com.dennytech.resamopro.ui.screen.main.profile.ProfileFragment
import com.google.gson.GsonBuilder

@Composable
fun MainNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = MainScreen.Home.route,
    ) {

        composable(route = MainScreen.Home.route) {
            HomeFragment()
        }

        composable(route = MainScreen.Account.route) {
            ProfileFragment()
        }

        composable(route = MainScreen.Notification.route) {
            ProductsFragment()
        }

//        composable("detail/{transaction}") { navBackStackEntry ->
//            // Creating gson object
//            val gson = GsonBuilder().create()
//            /* Extracting the transaction object json from the route */
//            val userJson = navBackStackEntry.arguments?.getString("transaction")
//            // Convert json string to the Transaction data class object
//            val transactionObj = gson.fromJson(userJson, TransactionDomainModel::class.java)
//            TransactionDetailFragment(
//                transaction = transactionObj,
//                navigateUp = { navController.navigateUp() }
//            )
//        }
    }
}