package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.Grey200

@Composable
fun ProductLabel(
    modifier: Modifier = Modifier
        .padding(horizontal = Dimens._8dp, vertical = Dimens._4dp),
    title: String,
    containerColor: Color = Grey200,
    contentColor: Color = DeepSeaBlue,
    fontSize: TextUnit = Dimens._12sp,
) {

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens._0dp
        ),
        shape = RoundedCornerShape(Dimens._8dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
//            .border(
//                0.5.dp, RedLight400, RoundedCornerShape(Dimens._16dp)
//            ),
    ) {
        Text(
            modifier = modifier,
            text = title,
            fontSize = fontSize,
            color = contentColor,
            textAlign = TextAlign.Center
        )
    }
}