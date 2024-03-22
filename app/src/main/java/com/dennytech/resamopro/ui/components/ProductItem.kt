package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.Grey200
import com.dennytech.resamopro.ui.theme.TruliBlue
import com.dennytech.resamopro.ui.theme.TruliBlueLight900
import com.dennytech.resamopro.ui.theme.TruliLightBlue
import com.dennytech.resamopro.ui.theme.White50
import com.dennytech.resamopro.utils.Helpers.capitalize

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: ProductDomainModel,
    onClick: () -> Unit,
    preview: () -> Unit
) {

    Box(modifier = Modifier) {
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

                Column {
                    ConstraintLayout {
                        val (button, text) = createRefs()

                        TranImage(product = product, onClick = {})

                       Row(
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(start = Dimens._8dp),
                           horizontalArrangement = Arrangement.SpaceBetween,
                           verticalAlignment = Alignment.CenterVertically
                       ) {
                           ProductLabel(
                               title = product.quantity.toString(),
                               containerColor = TruliBlueLight900,
                           )
                           IconButton(onClick = { preview() }) {
                               VisibilityIcon(tint = TruliBlue)
                           }
                       }
                    }


                    Column (modifier = Modifier.padding(vertical = Dimens._14dp)){
                        Row(
                            modifier = Modifier
                                .padding(horizontal = Dimens._14dp)
                                .wrapContentSize(Alignment.Center),
                        ) {
                            ProductLabel(title = product.size.toString())
                            Spacer(modifier = Modifier.width(Dimens._6dp))
                            ProductLabel(title = product.color.capitalize())
                        }

                        Spacer(modifier = Modifier.height(Dimens._6dp))
                        Row(
                            modifier = Modifier.padding(horizontal = Dimens._14dp)
                        ) {
                            ProductLabel(title = product.type.capitalize())
                            Spacer(modifier = Modifier.width(Dimens._6dp))
                            ProductLabel(title = product.brand.capitalize())
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TranImage(
    product: ProductDomainModel,
    onClick: () -> Unit
) {

    SubcomposeAsyncImage(
        model = product.thumbnail,
        contentDescription = "product image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(RoundedCornerShape(size = Dimens._8dp))
            .height(Dimens._150dp),
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


@Composable
fun StaggeredHorizontalList(
    data: List<String>,
    modifier: Modifier = Modifier,
    itemModifier: Modifier = Modifier,
    stagger: Int = 2, // Number of items per row
    itemSpacing: Dp = Dimens._8dp, // Space between items
    contentPadding: PaddingValues = PaddingValues(horizontal = Dimens._8dp, vertical = Dimens._8dp)
) {
    LazyColumn(modifier = modifier, contentPadding = contentPadding) {
//        data.toList().chunked(stagger) { rowItems ->
//            ItemRow(rowItems = rowItems, modifier = itemModifier, itemSpacing = itemSpacing)
//        }
        items(data) {
            ProductLabel(title = it.capitalize())
        }
    }
}

@Composable
private fun ItemRow(
    rowItems: List<String>,
    modifier: Modifier = Modifier,
    itemSpacing: Dp = Dimens._8dp // Space between items
) {
    Row(modifier = modifier) {
        rowItems.forEachIndexed { index, item ->
            if (index > 0) {
                Spacer(modifier = Modifier.width(itemSpacing))
            }

        }
    }
}