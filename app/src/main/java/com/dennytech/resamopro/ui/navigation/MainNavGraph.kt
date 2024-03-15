package com.dennytech.resamopro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.resamopro.ui.screen.auth.onboarding.OnBoardingRootFragment
import com.dennytech.resamopro.ui.screen.main.home.HomeFragment
import com.dennytech.resamopro.ui.screen.main.products.ProductsFragment
import com.dennytech.resamopro.ui.screen.main.products.create.CreateProductFragment
import com.dennytech.resamopro.ui.screen.main.products.sale.RecordSaleFragment
import com.dennytech.resamopro.ui.screen.main.profile.ProfileFragment
import com.google.gson.GsonBuilder

@Composable
fun MainNavGraph(
    mainNavController: NavHostController,
    bottomNavController: NavHostController,
) {
    NavHost(
        navController = bottomNavController,
        route = Graph.MAIN,
        startDestination = MainScreen.Products.route,
    ) {

        composable(route = MainScreen.Home.route) {
            HomeFragment()
        }

        composable(route = MainScreen.Account.route) {
            ProfileFragment(
                navigateToAuth = {
                    mainNavController.navigate(Graph.AUTH) {
                        popUpTo(Graph.MAIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = MainScreen.NewProduct.route) {
            CreateProductFragment(
                navigateUp = { bottomNavController.navigateUp() },
            )
        }

        composable(route = MainScreen.Products.route) {
            ProductsFragment(
                navController = bottomNavController,
                navigateUp = { bottomNavController.navigateUp() },
                navigateToNewProduct = {
                    bottomNavController.navigate(MainScreen.NewProduct.route)
                }
            )
        }

        composable("detail/{product}") { navBackStackEntry ->
            // Creating gson object
            val gson = GsonBuilder().create()
            /* Extracting the transaction object json from the route */
            val userJson = navBackStackEntry.arguments?.getString("product")
            // Convert json string to the Transaction data class object
            val transactionObj = gson.fromJson(userJson, ProductDomainModel::class.java)
            RecordSaleFragment(
                product = transactionObj,
                navigateUp = { bottomNavController.navigateUp() },
                navigateToProducts = {
                    bottomNavController.navigate(MainScreen.Products.route)
                }
            )
        }
    }
}