package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens

@Composable
fun LeftRightLabel(
    startText: String,
    endText: String,
    onActionClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = startText,
            textAlign = TextAlign.Center,
            fontSize = Dimens._16sp,
            color = DeepSeaBlue
        )

        TextButton(
            onClick = { onActionClick() },
        ) {
            Text(endText)
        }
    }
}