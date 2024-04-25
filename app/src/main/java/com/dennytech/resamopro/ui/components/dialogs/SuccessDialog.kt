package com.dennytech.resamopro.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dennytech.resamopro.ui.components.CheckCircleIcon
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.Grey100
import com.dennytech.resamopro.ui.theme.TruliBlue
import com.dennytech.resamopro.ui.theme.TruliGreen

@Composable
fun SuccessDialog(
    dismissDialog: () -> Unit, message: String
) {

    Dialog(properties = DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = { }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens._14dp),
            colors = CardDefaults.cardColors(
                containerColor = Grey100,
            ),
            shape = RoundedCornerShape(Dimens._16dp),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(Dimens._16dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Spacer(modifier = Modifier.height(Dimens._20dp))
                    CheckCircleIcon(
                        modifier = Modifier
                            .height(Dimens._80dp)
                            .width(Dimens._80dp), tint = TruliGreen
                    )
                    Spacer(modifier = Modifier.height(Dimens._20dp))

                    Text(
                        text = message,
                        fontSize = Dimens._18sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(Dimens._20dp))

                    TextButton(
                        onClick = dismissDialog,
                        modifier = Modifier.padding(horizontal = Dimens._30dp, vertical = Dimens._12dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TruliBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        }
    }
}