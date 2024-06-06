package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dennytech.resamopro.ui.theme.Dimens

@Composable
fun EmptyComponent() {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = Dimens._16dp, vertical = Dimens._16dp).fillMaxWidth()
    ) {

        DiamondIcon()
        Text(text = "No data", color = Color.Gray)
    }
}