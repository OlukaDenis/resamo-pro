package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliRed
import com.dennytech.resamopro.ui.theme.RedLight800
import com.dennytech.resamopro.ui.theme.RedLight400

@Composable
fun ErrorLabel(
    message: String,
    modifier: Modifier = Modifier
) {

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens._0dp
        ),
        shape = RoundedCornerShape(Dimens._16dp),
        colors = CardDefaults.cardColors(
            containerColor = RedLight800,
        ),
        modifier = modifier
            .border(
                0.5.dp, RedLight400, RoundedCornerShape(Dimens._16dp)
            ),
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = Dimens._16dp, vertical = Dimens._4dp),
            text = message,
            fontSize = Dimens._14sp,
            color = TruliRed,
            textAlign = TextAlign.Center
        )
    }
}