package com.dennytech.resamopro

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.dennytech.resamopro.ui.navigation.ResamoNavHost
import com.dennytech.resamopro.ui.theme.ResamoProTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: RootViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.loading.value
            }
        }

        setContent {
            MainContent()
        }
    }
}

@Composable
private fun MainContent() {
    val navController = rememberNavController()

//    val appThemeState = mainViewModel.appTheme.collectAsStateWithLifecycle()
//    val darkTheme: Boolean = when (appThemeState.value) {
//        AppTheme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
//        AppTheme.DARK_THEME -> true
//        AppTheme.LIGHT_THEME -> false
//    }

    ResamoProTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ResamoNavHost(navHostController = navController)
        }
    }
}