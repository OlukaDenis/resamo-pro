package com.dennytech.resamopro.ui.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.resamopro.ui.screen.main.products.ProductEvent
import com.dennytech.resamopro.ui.screen.main.products.ProductViewModel
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.Grey100
import com.dennytech.resamopro.utils.Helpers

@Composable
fun FilterDialog(
    viewModel: ProductViewModel = hiltViewModel(),
    dismissDialog: () -> Unit,
    confirm: () -> Unit,
) {

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { dismissDialog() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens._14dp),
            colors = CardDefaults.cardColors(
                containerColor = Grey100,
            ),
            shape = RoundedCornerShape(Dimens._8dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(Dimens._16dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Filter Products",
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimens._18sp
                    )
                    TextButton(
                        onClick = { viewModel.onEvent(ProductEvent.ClearFilters) },
                        modifier = Modifier.padding(Dimens._12dp),
                    ) {
                        Text("Clear")
                    }
                }
                Spacer(modifier = Modifier.height(Dimens._14dp))
                Divider()
                Spacer(modifier = Modifier.height(Dimens._16dp))

                FilterLabel("Enter size")
                CustomTextField(
                    value = viewModel.state.filters.size,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { viewModel.onEvent(ProductEvent.SetSizeFilter(it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                FilterLabel("Enter brand")
                CustomTextField(
                    value = viewModel.state.filters.brand,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { viewModel.onEvent(ProductEvent.SetBrandFilter(it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                FilterLabel("Enter color")
                CustomTextField(
                    value = viewModel.state.filters.color,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { viewModel.onEvent(ProductEvent.SetColorFilter(it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                FilterLabel("Select shoe type")
                CustomExposedDropdown(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "",
                    onValueChange = { viewModel.onEvent(ProductEvent.SetTypeFilter(it)) },
                    items = Helpers.shoeTypes()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { dismissDialog() },
                        modifier = Modifier.padding(Dimens._12dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = { confirm() },
                        modifier = Modifier.padding(Dimens._12dp),
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }

//    AlertDialog(
//        title = {
//            Text(text = "dialogTitle")
//        },
//        text = {
//            Text(text = "dialogText")
//        },
//        onDismissRequest = {
//            dismissDialog()
//        },
//        confirmButton = {
//            TextButton(
//                onClick = {
//                    dismissDialog()
//                }
//            ) {
//                Text("Confirm")
//            }
//        },
//        dismissButton = {
//            TextButton(
//                onClick = {
//                    dismissDialog()
//                }
//            ) {
//                Text("Dismiss")
//            }
//        }
//    )
}

@Composable
private fun FilterLabel(title: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = title,
            modifier = modifier,
            fontSize = Dimens._16sp
        )
        Spacer(modifier = Modifier.height(Dimens._8dp))
    }
}