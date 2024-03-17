package com.dennytech.resamopro.ui.screen.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.CustomButton

@Composable
fun HomeFragment(
    mainViewModel: MainViewModel = hiltViewModel(),
    navigateToSales: () -> Unit
) {

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {

            CustomButton(title = "Check", onClick = {navigateToSales()})
        }
    }

}