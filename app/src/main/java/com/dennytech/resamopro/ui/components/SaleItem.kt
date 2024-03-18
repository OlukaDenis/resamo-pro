package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.min
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.dennytech.domain.models.SaleDomainModel
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliBlue
import com.dennytech.resamopro.ui.theme.TruliGreen
import com.dennytech.resamopro.utils.Helpers.formatCurrency
import com.dennytech.resamopro.utils.Helpers.formatDate

@Composable
fun SaleItem(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
    sale: SaleDomainModel,
    onClick: () -> Unit,
    onItemSelected: (String) -> Unit
) {

    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val dropdownList = mutableListOf<String>().apply {
        if (!sale.collected) this.add("Confirm")
    }

    Box(modifier = Modifier.widthIn(min = Dimens._150dp)) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = Dimens._0dp
            ),
            shape = RoundedCornerShape(Dimens._16dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = modifier.fillMaxWidth()
        ) {

            Row(modifier = modifier
                .clickable {
                    onClick()
                    isContextMenuVisible = true
                }
                .fillMaxWidth()
            ) {

                Column() {

                    TranImage(sale = sale)
                    Column (modifier = Modifier.padding(Dimens._10dp).fillMaxWidth()){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = sale.sellingPrice.toDouble().formatCurrency(),
                                textAlign = TextAlign.Center,
                                fontSize = Dimens._16sp,
                                color = DeepSeaBlue
                            )

                            CheckCircleIcon(
                                modifier = Modifier.size(Dimens._16dp),
                                tint = if(sale.collected) TruliGreen else Color.Gray
                            )
                        }

                        DropdownMenu(
                            expanded = isContextMenuVisible,
                            onDismissRequest = { isContextMenuVisible = false }
                        ) {

                            dropdownList.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        onItemSelected(sale.id)
                                        isContextMenuVisible = false
                                    }
                                )
                            }
                        }

                      mainViewModel.state.user?.let {
                          if (it.role == 1) {
                              Column {
                                  VerticalSpacer(Dimens._4dp)
                                  Row(
                                      horizontalArrangement = Arrangement.Center,
                                      verticalAlignment = Alignment.CenterVertically
                                  ) {
                                      Text(
                                          text = "Revenue: ",
                                          textAlign = TextAlign.Center,
                                          fontSize = Dimens._10sp,
                                          color = TruliBlue
                                      )
                                      Text(
                                          text = sale.profit.toDouble().formatCurrency(),
                                          textAlign = TextAlign.Center,
                                          fontSize = Dimens._14sp,
                                          color = TruliBlue,
                                          fontWeight = FontWeight.Bold
                                      )
                                  }
                              }
                          }
                      }

                        Text(
                            text = sale.saleDate.formatDate(),
                            textAlign = TextAlign.Center,
                            fontSize = Dimens._12sp,
                            color = Color.Gray,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TranImage(
    sale: SaleDomainModel
) {

    SubcomposeAsyncImage(
        model = sale.product?.thumbnail,
        contentDescription = "product image",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .clip(RoundedCornerShape(size = Dimens._8dp))
            .widthIn(min = Dimens._150dp)
            .height(Dimens._100dp),
    ) {
        val state = painter.state
        if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens._50dp),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_gray_bg),
                contentDescription = "avatar"
            )
        } else {
            SubcomposeAsyncImageContent()
        }
    }
}