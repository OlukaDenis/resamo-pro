package com.dennytech.resamopro.ui.components.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.RedLight400
import com.dennytech.resamopro.ui.theme.RedLight800
import com.dennytech.resamopro.ui.theme.TruliRed

@Composable
fun NotActivatedCard() {

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens._0dp
        ),
        shape = RoundedCornerShape(Dimens._8dp),
        colors = CardDefaults.cardColors(
            containerColor = RedLight800,
        ),
        modifier = Modifier
            .border(
                0.5.dp, RedLight400, RoundedCornerShape(Dimens._16dp)
            ),
    ) {
        Column(
            modifier = Modifier.padding(Dimens._16dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Info",
                fontSize = Dimens._18sp,
                color = TruliRed,
                textAlign = TextAlign.Center
            )
            VerticalSpacer(Dimens._16dp)
            Text(
                modifier = Modifier,
                text = "Your account is not activated. Please contact your administrator!",
                fontSize = Dimens._14sp,
                color = TruliRed,
                textAlign = TextAlign.Center
            )
        }
    }
}

