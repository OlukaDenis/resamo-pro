package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import com.dennytech.domain.models.StoreUserDomainModel
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliGreen
import com.dennytech.resamopro.utils.Helpers.formatDateTime


@Composable
fun StoreUserItem(
    modifier: Modifier = Modifier,
    user: StoreUserDomainModel,
    loading: Boolean = false,
    onClick: (() -> Unit),
    onMenuClick: (String) -> Unit,
    containerColor: Color = Color.White,
) {

    var itemHeight by remember {
        mutableStateOf(Dimens._0dp)
    }

    val density = LocalDensity.current

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
                .onSizeChanged {
                    itemHeight = with(density) { it.height.toDp() }
                }
                .clickable { onClick() },
        ) {
            Row(
                modifier = Modifier
                    .padding(Dimens._16dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileIcon()
                    HorizontalSpacer(Dimens._8dp)
                    Column {
                        Row {

                            Text(
                                text = user.fullName,
                                textAlign = TextAlign.Center,
                                fontSize = Dimens._14sp
                            )
                            HorizontalSpacer(Dimens._4dp)
                            ProductLabel(
                                modifier = Modifier.padding(
                                    horizontal = Dimens._4dp,
                                    vertical = Dimens._2dp
                                ),
                                title = when(user.role) {
                                    0 -> "Owner"
                                    1 -> "Manager"
                                    else -> "Employee"
                                },
                                fontSize = Dimens._9sp
                            )
                        }
                        Text(
                            text = user.email,
                            fontSize = Dimens._14sp,
                            color = Color.Gray
                        )
                    }
                }
                Column {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (loading) CircularProgressIndicator()
                        else CheckCircleIcon(
                            tint = if (user.status == 1) TruliGreen else defaultIconTint()
                        )
                    }
                }
            }
        }


    }


}