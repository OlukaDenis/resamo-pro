package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.dennytech.domain.models.TransactionDomainModel
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.Green800
import com.dennytech.resamopro.ui.theme.Orange800
import com.dennytech.resamopro.ui.theme.RedLight800
import com.dennytech.resamopro.ui.theme.TruliGreen
import com.dennytech.resamopro.ui.theme.TruliOrange
import com.dennytech.resamopro.ui.theme.TruliRed

@Composable
fun TranStatusText(
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    transaction: TransactionDomainModel
) {

    val color = when (transaction.status) {
        "success" -> TruliGreen
        "failed" -> TruliRed
        "pending" -> TruliOrange
        else -> TruliOrange
    }

    val bgColor = when (transaction.status) {
        "success" -> Green800
        "failed" -> RedLight800
        "pending" -> RedLight800
        else -> Orange800
    }

    val status = when (transaction.status) {
        "success" -> "Delivered"
        "failed" -> "Failed"
        "pending" -> "Pending"
        else -> "Pending"
    }

    val exclude = mutableListOf<String>().apply {
        this.add("fee")
    }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens._0dp
        ),
        shape = RoundedCornerShape(Dimens._8dp),
        colors = CardDefaults.cardColors(
            containerColor = bgColor,
        ),
    ) {
        if (!exclude.contains(transaction.type)) {
            Text(
                text = status.uppercase(),
                modifier = Modifier
                    .padding(horizontal = Dimens._8dp, vertical = Dimens._4dp),
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}