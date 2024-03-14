package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.dennytech.resamopro.R
import com.dennytech.domain.models.TransactionDomainModel
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliBlue
import com.dennytech.resamopro.utils.Helpers.formatCurrency
import com.dennytech.resamopro.utils.Helpers.formatDate
import com.dennytech.resamopro.utils.Helpers.pickInitials
import java.util.Locale

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    transaction: TransactionDomainModel,
    onClick: () -> Unit
) {

    Box(modifier = Modifier.padding(vertical = Dimens._3dp)) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = Dimens._0dp
            ),
            shape = RoundedCornerShape(Dimens._16dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
        ) {

            Row(modifier = Modifier
                .clickable { onClick() }) {

                Column(
                    modifier = Modifier.padding(Dimens._16dp)

                ) {

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {

                            TranImage(transaction = transaction)

                            Spacer(modifier = Modifier.width(Dimens._6dp))

                            Column {
                                TitleText(transaction = transaction)
                                Spacer(modifier = Modifier.height(Dimens._6dp))
                                Text(
                                    text = transaction.createdAt.formatDate(),
                                    fontSize = Dimens._10sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            TranStatusText(transaction = transaction, fontSize = Dimens._9sp)
                            Spacer(modifier = Modifier.height(Dimens._6dp))
                            Text(
                                text = transaction.amount.formatCurrency(transaction.currency),
                                fontSize = Dimens._14sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepSeaBlue
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TitleText(
    modifier: Modifier = Modifier,
    transaction: TransactionDomainModel
) {
    val label = when {
        transaction.receiver != null -> transaction.receiver!!.fullName
        transaction.sender != null -> transaction.sender!!.fullName
        transaction.type == "payment" -> "Wallet Charge"
        else -> transaction.type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

    Text(
        text = label,
        fontSize = Dimens._14sp,
        fontWeight = FontWeight.Bold,
        color = DeepSeaBlue
    )
}

@Composable
private fun TranImage(
    transaction: TransactionDomainModel
) {


    val url = when {
        transaction.receiver != null -> transaction.receiver!!.fullName
        transaction.sender != null -> transaction.sender!!.fullName
        else -> ""
    }

    SubcomposeAsyncImage(
        model = url,
        contentDescription = "contentDescription",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .clip(CircleShape)
            .width(Dimens._32dp)
            .height(Dimens._32dp),
    ) {
        val state = painter.state
        if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {

            if (transaction.receiver == null && transaction.sender == null) {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .width(Dimens._32dp)
                        .height(Dimens._32dp),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.ic_diamond),
                    contentDescription = "avatar"
                )
            } else {
                TranImagePlaceHolder(transaction = transaction)
            }

        } else {
            SubcomposeAsyncImageContent()
        }
    }
}

@Composable
fun TranImagePlaceHolder(
    transaction: TransactionDomainModel
) {

    val username = when {
        transaction.receiver != null -> transaction.receiver?.fullName!!.pickInitials()
        transaction.sender != null -> transaction.sender?.fullName!!.pickInitials()
        else -> "TP"
    }
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens._0dp
        ),
        shape = RoundedCornerShape(Dimens._50dp),
        colors = CardDefaults.cardColors(
            containerColor = TruliBlue,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = username,
                    style = TextStyle(color = Color.White)
                )
            }
        }
    }
}