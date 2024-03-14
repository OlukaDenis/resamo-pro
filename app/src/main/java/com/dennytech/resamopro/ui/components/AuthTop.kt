package com.dennytech.resamopro.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.Grey100
import com.dennytech.resamopro.ui.theme.Grey600

@Composable
fun AuthTop(
    @DrawableRes drawable: Int,
    title: String,
    description: String,
    onBack: (() -> Unit?)? = null,
) {
    Column(
        modifier = Modifier
            .background(Grey100)
    ) {

        Box(
            modifier = Modifier
                .height(Dimens._300dp)
                .background(Grey600)
        ) {

            onBack?.let {
                Column {
                    Spacer(modifier = Modifier.height(Dimens._8dp))
                    IconButton(onClick = { onBack()}) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.White,
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val painter = painterResource(id = drawable)
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .height(Dimens._80dp)
                        .width(Dimens._80dp)
                )
                Spacer(modifier = Modifier.height(Dimens._40dp))
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = Dimens._28sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(Dimens._18dp))
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}