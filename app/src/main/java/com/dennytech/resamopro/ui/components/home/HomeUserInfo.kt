package com.dennytech.resamopro.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.CircleIcon
import com.dennytech.resamopro.ui.components.HorizontalSpacer
import com.dennytech.resamopro.ui.components.ProfileIcon
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.SageColor

@Composable
fun HomeUserInfo(
    mainViewModel: MainViewModel = hiltViewModel(),
) {

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            CircleIcon(
                onClick = {},
                icon = {
                    ProfileIcon(
                        modifier = Modifier.size(Dimens._30dp),
                        tint = Color.Gray
                    )
                },
            )
            HorizontalSpacer(Dimens._8dp)
            Column {
                Text(
                    text = "Hello, ",
                    textAlign = TextAlign.Center,
                    fontSize = Dimens._16sp,
                    color = DeepSeaBlue
                )
                Text(
                    text = mainViewModel.state.user?.firstName ?: "",
                    textAlign = TextAlign.Center,
                    fontSize = Dimens._18sp,
                    color = DeepSeaBlue
                )
            }
        }
    }
}