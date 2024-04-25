package com.dennytech.resamopro.ui.screen.main.products.sale

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.components.CalendarDatePicker
import com.dennytech.resamopro.ui.components.CalendarIcon
import com.dennytech.resamopro.ui.components.CustomButton
import com.dennytech.resamopro.ui.components.CustomTextField
import com.dennytech.resamopro.ui.components.dialogs.SuccessDialog
import com.dennytech.resamopro.ui.theme.Dimens
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordSaleFragment(
    viewModel: RecordSaleViewModel = hiltViewModel(),
    navigateToProducts: () -> Unit,
    navigateUp: () -> Unit,
    product: ProductDomainModel
) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Record Sale",
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateUp() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { padding ->

        viewModel.onEvent(RecordSaleEvent.SetProduct(product.id))

        if (viewModel.requestComplete) {
            SuccessDialog(
                dismissDialog = {
                    navigateToProducts()
                },
                message = "Successfully recorded a sale."
            )

        }

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            Column {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens._14dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    shape = RoundedCornerShape(Dimens._8dp),
                ) {
                    SubcomposeAsyncImage(
                        model = product.thumbnail,
                        contentDescription = "product image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(RoundedCornerShape(size = Dimens._8dp))
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

                Spacer(modifier = Modifier.height(Dimens._30dp))

                Column(
                    modifier = Modifier
                        .padding(Dimens._16dp),
                ) {

                    CustomTextField(
                        value = viewModel.state.price,
                        onValueChange = { viewModel.onEvent(RecordSaleEvent.PriceChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Selling Price",
                        isError = viewModel.state.priceError.isNotEmpty(),
                        errorMessage = viewModel.state.priceError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                    )
                    Spacer(modifier = Modifier.height(Dimens._16dp))

                    CustomTextField(
                        value = viewModel.state.quantity,
                        onValueChange = { viewModel.onEvent(RecordSaleEvent.QuantityChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Quantity",
                        isError = viewModel.state.quantityError.isNotEmpty(),
                        errorMessage = viewModel.state.quantityError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                    )
                    Spacer(modifier = Modifier.height(Dimens._16dp))

                    Column(
                    ) {
                        Card(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = Dimens._0dp
                            ),
                            shape = RoundedCornerShape(Dimens._16dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier
                                    .clickable {
                                        viewModel.onEvent(RecordSaleEvent.ToggleSaleDatePicker)
                                    },
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(Dimens._56dp)
                                        .padding(Dimens._16dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        text = viewModel.state.saleDateText.ifEmpty { "Sale Date" },
                                        color = if(viewModel.state.saleDate.isEmpty()) Color.Gray else Color.Black
                                    )
                                    CalendarIcon(tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }

                        if (viewModel.state.saleDateError.isNotEmpty()) {
                            Text(
                                viewModel.state.saleDateError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(start = Dimens._8dp)
                            )
                        }

//                        CustomTextField(
//                            value = viewModel.state.saleDate,
//                            onValueChange = { viewModel.onEvent(RecordSaleEvent.SaleDateChanged(it)) },
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                            placeholder = "Sale Date",
//                            readOnly = true,
//                            isError = viewModel.state.saleDateError.isNotEmpty(),
//                            errorMessage = viewModel.state.saleDateError,
//                            keyboardOptions = KeyboardOptions(
//                                keyboardType = KeyboardType.Text,
//                            ),
//                            trailingIcon = {
//                                CalendarIcon(tint = MaterialTheme.colorScheme.primary)
//                            }
//                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens._30dp))

                    CustomButton(title = stringResource(R.string.record_sale),
                        modifier = Modifier.weight(1f),
                        loading = viewModel.state.loading,
                        onClick = { viewModel.onEvent(RecordSaleEvent.Submit) })

                    Spacer(modifier = Modifier.height(Dimens._20dp))
                }
            }

            if (viewModel.state.showSaleDatePicker) {
                CalendarDatePicker(
                    useTimeMillis = true,
                    showDatePicker = viewModel.state.showSaleDatePicker,
                    toggleCalender = { viewModel.onEvent(RecordSaleEvent.ToggleSaleDatePicker) },
                    onDateChanged = { viewModel.onEvent(RecordSaleEvent.SaleDateChanged(it)) }
                )
            }
        }

    }
}