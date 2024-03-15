package com.dennytech.resamopro.ui.screen.auth.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.resamopro.utils.Helpers.isAccessTokenExpired
import timber.log.Timber

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
                viewModel.expiryTime == 0L -> {
                    navigateToLogin()
                }
                viewModel.expiryTime.isAccessTokenExpired() -> {
                    navigateToLogin()
                }
                else -> {
                    navigateToHome()
                }
            }
        }
    }
}