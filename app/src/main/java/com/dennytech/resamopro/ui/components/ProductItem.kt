package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.Grey200
import com.dennytech.resamopro.utils.Helpers.capitalize

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: ProductDomainModel,
    onClick: () -> Unit
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
                    TranImage(product = product)

//                   Spacer(modifier = Modifier.height(Dimens._8dp))

                    Column (modifier = Modifier.padding(vertical = Dimens._14dp)){
                        Row(
                            modifier = Modifier
                                .padding(horizontal = Dimens._14dp)
                                .wrapContentSize(Alignment.Center),
                        ) {
                            ProductLabel(title = product.size.toString(), color = Grey200)
                            Spacer(modifier = Modifier.width(Dimens._6dp))
                            ProductLabel(title = product.color.capitalize(), color = Grey200)
                        }

                        Spacer(modifier = Modifier.height(Dimens._6dp))
                        Row(
                            modifier = Modifier.padding(horizontal = Dimens._14dp)
                        ) {
                            ProductLabel(title = product.type.capitalize(), color = Grey200)
                            Spacer(modifier = Modifier.width(Dimens._6dp))
                            ProductLabel(title = product.brand.capitalize(), color = Grey200)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TranImage(
    product: ProductDomainModel
) {

    SubcomposeAsyncImage(
        model = product.image,
        contentDescription = "product image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(RoundedCornerShape(size = Dimens._8dp))
//            .clip(CircleShape)
//            .width(Dimens._32dp)
            .height(Dimens._200dp),
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
            ProductLabel(title = it.capitalize(), color = Grey200)
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