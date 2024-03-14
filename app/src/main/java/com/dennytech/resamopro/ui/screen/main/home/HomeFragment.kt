package com.dennytech.resamopro.ui.screen.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeFragment() {

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
        }
    }

}