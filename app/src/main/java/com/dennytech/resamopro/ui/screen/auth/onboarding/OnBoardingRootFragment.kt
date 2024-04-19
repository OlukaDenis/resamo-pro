package com.dennytech.resamopro.ui.screen.auth.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.data.utils.isAccessTokenExpired

@Composable
fun OnBoardingRootFragment(
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: OnBoardingViewModel = hiltViewModel()
) {

//    LaunchedEffect(Unit) {
//        viewModel.fetchLocationData()
//    }

    Scaffold {

        Box(modifier = Modifier.padding(it)) {
            when {
                viewModel.expiryTime == 0L ||
                        viewModel.expiryTime.isAccessTokenExpired() ||
                        viewModel.currentStore.isEmpty() -> {
                    navigateToLogin()
                }

                else -> {
                    navigateToHome()
                }
            }
        }
    }
}