package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.dennytech.resamopro.ui.theme.Dimens

@Composable
fun HorizontalSpacer(
    width: Dp = Dimens._0dp
) {
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun VerticalSpacer(
    height: Dp = Dimens._0dp
) {
    Spacer(modifier = Modifier.height(height))
}