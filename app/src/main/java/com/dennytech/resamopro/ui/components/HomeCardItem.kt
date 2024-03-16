package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliBlueLight900

@Composable
fun HomeCardItem(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit),
    containerColor: Color = Color.White,
    title: String,
    icon: @Composable (() -> Unit)
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens._0dp
        ),
        shape = RoundedCornerShape(Dimens._8dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
        modifier = modifier
            .padding(Dimens._4dp)
            .fillMaxWidth()
    ) {
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .clickable { onClick() },
       ) {
           Row(
               modifier = Modifier.padding(Dimens._16dp),
               horizontalArrangement = Arrangement.Start,
               verticalAlignment = Alignment.CenterVertically
           ) {
              icon()
               HorizontalSpacer(Dimens._8dp)
               Text(
                   text = title,
                   textAlign = TextAlign.Center,
                   fontSize = Dimens._14sp
               )
           }
       }
    }
}

@Composable
fun HomeCircleIcon(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens._0dp
        ),
        shape = RoundedCornerShape(Dimens._50dp),
        colors = CardDefaults.cardColors(
            containerColor = TruliBlueLight900,
        ),
    ) {
        Column(
            modifier = Modifier.padding(Dimens._8dp),
        ) {
            icon()
        }
    }
}


