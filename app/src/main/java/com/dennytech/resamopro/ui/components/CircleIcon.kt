package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dennytech.resamopro.ui.theme.Dimens


@Composable
fun CircleIcon(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit),
    containerColor: Color = Color.White,
    icon: @Composable (() -> Unit)
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens._0dp
        ),
        shape = RoundedCornerShape(Dimens._50dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
        modifier = modifier
    ) {
        Row(modifier = Modifier.clickable { onClick() }) {
            Column(
                modifier = Modifier
                    .padding(Dimens._12dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                icon()
            }
        }
    }
}

