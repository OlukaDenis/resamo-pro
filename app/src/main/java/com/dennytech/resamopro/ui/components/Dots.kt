package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.Grey200
import com.dennytech.resamopro.ui.theme.TruliBlue

@Composable
fun Dots(modifier: Modifier, index: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Row {
            Canvas(modifier = Modifier.size(Dimens._8dp), onDraw = {
                drawCircle(color = if(index == 1) TruliBlue else Grey200)
            })
            Spacer(modifier = Modifier.width(Dimens._8dp))
            Canvas(modifier = Modifier.size(Dimens._8dp), onDraw = {
                drawCircle(color = if(index == 2) TruliBlue else Grey200)
            })
            Spacer(modifier = Modifier.width(Dimens._8dp))
            Canvas(modifier = Modifier.size(Dimens._8dp), onDraw = {
                drawCircle(color = if(index == 3) TruliBlue else Grey200)
            })
        }
    }

}