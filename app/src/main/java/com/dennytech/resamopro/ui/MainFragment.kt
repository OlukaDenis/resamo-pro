package com.dennytech.resamopro.ui

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.navigation.MainNavGraph
import com.dennytech.resamopro.ui.navigation.MainScreen
import com.dennytech.resamopro.ui.navigation.navigateToMainScreen
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliBlue
import com.dennytech.resamopro.ui.theme.TruliBlueLight900

@Composable
fun MainFragment(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val bottomNavController = rememberNavController()
    val currentSelectedScreen by bottomNavController.currentScreenAsState()

    val view = LocalView.current

    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = Color.White.toArgb()
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = bottomNavController,
                currentSelectedScreen = currentSelectedScreen
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            MainNavGraph(
                mainNavController = navController,
                bottomNavController = bottomNavController
            )
        }
    }
}


@Composable
private fun BottomNavBar(
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavController,
    currentSelectedScreen: MainScreen,
) {
    LaunchedEffect(key1 = true) {
        mainViewModel.initiate()
    }

    val colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = Color.Gray,
        unselectedTextColor = Color.Gray,
        indicatorColor = TruliBlueLight900
    )

    val user = mainViewModel.state.user ?: return

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = Dimens._8dp
    ) {

        NavigationBarItem(
            selected = currentSelectedScreen == MainScreen.Home,
            onClick = { navController.navigate(MainScreen.Home.route) },
            alwaysShowLabel = false,
            colors = colors,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_diamond),
                    contentDescription = stringResource(id = R.string.home),
                    tint = if (currentSelectedScreen == MainScreen.Home) TruliBlue else Color.Gray,
                )
            }
        )

        if (user.status == 1) {
            NavigationBarItem(
                selected = currentSelectedScreen == MainScreen.Products,
                onClick = { navController.navigate(MainScreen.Products.route) },
                alwaysShowLabel = false,
                colors = colors,
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Store,
                        contentDescription = stringResource(id = R.string.products),
                        tint = if (currentSelectedScreen == MainScreen.Products) TruliBlue else Color.Gray,
                    )
                }
            )

//        NavigationBarItem(
//            selected = currentSelectedScreen == MainScreen.NewProduct,
//            onClick = { navController.navigateToMainScreen(MainScreen.NewProduct) },
//            alwaysShowLabel = false,
//            colors = colors,
//            icon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_transfer),
//                    contentDescription = stringResource(id = R.string.products),
//                    tint = if (currentSelectedScreen == MainScreen.NewProduct) TruliBlue else Color.Gray,
//                )
//            }
//        )

            NavigationBarItem(
                selected = currentSelectedScreen == MainScreen.Account,
                onClick = { navController.navigateToMainScreen(MainScreen.Account) },
                alwaysShowLabel = false,
                colors = colors,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_profile),
                        contentDescription = stringResource(id = R.string.account),
                        tint = if (currentSelectedScreen == MainScreen.Account) TruliBlue else Color.Gray,
                    )
                }
            )
        }
    }
}

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<MainScreen> {
    val selectedItem = remember { mutableStateOf<MainScreen>(MainScreen.Home) }
    DisposableEffect(key1 = this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == MainScreen.Products.route } -> {
                    selectedItem.value = MainScreen.Products
                }
                destination.hierarchy.any { it.route == MainScreen.Home.route } -> {
                    selectedItem.value = MainScreen.Home
                }
                destination.hierarchy.any { it.route == MainScreen.Account.route } -> {
                    selectedItem.value = MainScreen.Account
                }
            }

        }
        addOnDestinationChangedListener(listener)
        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return selectedItem
}
