package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dennytech.resamopro.ui.theme.Dimens

@Composable
fun CustomButton(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    loading: Boolean = false,
) {

    Row(
        modifier = Modifier.defaultMinSize()
    ) {
        Button(
            onClick = {
                if (!loading) onClick()
            },
            shape = RoundedCornerShape(Dimens._12dp),
            modifier = modifier.height(Dimens._52dp),
            colors = colors,
        ) {
            if (loading) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = Dimens._2dp,
                        modifier = Modifier
                            .height(Dimens._24dp)
                            .width(Dimens._24dp)
                    )
                }
            } else {
                Text(title, fontSize = Dimens._20sp)
            }
        }
    }

}