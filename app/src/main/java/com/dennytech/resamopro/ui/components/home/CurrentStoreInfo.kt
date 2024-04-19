package com.dennytech.resamopro.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.dennytech.resamopro.ui.components.HorizontalSpacer
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens

@Composable
fun CurrentStoreInfo(
    storeName: String,
    valueTextSize: TextUnit = Dimens._16sp,
    backgroundColor: Color = Color.White,
    foregroundColor: Color? = null,
    onClick: () -> Unit
) {

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens._0dp
        ),
        shape = RoundedCornerShape(Dimens._8dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
        ),
        modifier = Modifier.clickable { onClick() },
    ) {
        Column(
            modifier = Modifier.clickable { onClick() },
        ) {

            Row(
                modifier = Modifier.padding(Dimens._8dp),
//            horizontalArrangement = Arrangement.SpaceBetween
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Rounded.Storefront,
                    contentDescription = "Keyboard down",
                    tint = DeepSeaBlue,
                    modifier = Modifier.size(Dimens._18dp)
                )
                HorizontalSpacer(Dimens._3dp)
                Text(
                    text = storeName,
                    textAlign = TextAlign.Center,
                    fontSize = valueTextSize,
                    color = foregroundColor ?: DeepSeaBlue,
                    fontWeight = FontWeight.Bold
                )

                HorizontalSpacer(Dimens._16dp)

                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "Keyboard down",
                    tint = Color.Gray,
                    modifier = Modifier.size(Dimens._30dp)
                )
            }
        }

    }
}