package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliRed
import com.dennytech.resamopro.ui.theme.RedLight800
import com.dennytech.resamopro.ui.theme.RedLight400

@Composable
fun ErrorLabel(
    modifier: Modifier = Modifier,
    message: String,
    retry: Boolean = false,
    onRetry: () -> Unit = {},
) {

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens._0dp
        ),
        shape = RoundedCornerShape(Dimens._8dp),
        colors = CardDefaults.cardColors(
            containerColor = RedLight800,
        ),
        modifier = modifier
            .border(
                0.5.dp, RedLight400, RoundedCornerShape(Dimens._8dp)
            ).fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Dimens._16dp, vertical = Dimens._4dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = message,
                fontSize = Dimens._14sp,
                color = TruliRed,
                textAlign = TextAlign.Center
            )
            if (retry) {
                TextButton(onClick = { onRetry() }) {
                    Text(
                        text = "Retry",
                        fontSize = Dimens._14sp,
                        color = TruliRed,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}