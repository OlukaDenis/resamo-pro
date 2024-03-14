package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.Grey600
import com.dennytech.resamopro.ui.theme.RedLight400
import com.dennytech.resamopro.ui.theme.RedLight800
import com.dennytech.resamopro.ui.theme.TruliBlue
import com.dennytech.resamopro.ui.theme.TruliRed

@Composable
fun ProductLabel(
    title: String,
    color: Color,
    modifier: Modifier = Modifier
) {

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens._0dp
        ),
        shape = RoundedCornerShape(Dimens._8dp),
        colors = CardDefaults.cardColors(
            containerColor = color,
        ),
        modifier = modifier
//            .border(
//                0.5.dp, RedLight400, RoundedCornerShape(Dimens._16dp)
//            ),
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = Dimens._8dp, vertical = Dimens._4dp),
            text = title,
            fontSize = Dimens._14sp,
            color = DeepSeaBlue,
            textAlign = TextAlign.Center
        )
    }
}