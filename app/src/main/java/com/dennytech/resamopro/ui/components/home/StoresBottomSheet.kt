package com.dennytech.resamopro.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.components.CircleIcon
import com.dennytech.resamopro.ui.components.HorizontalSpacer
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.screen.main.home.HomeEvent
import com.dennytech.resamopro.ui.screen.main.home.HomeViewModel
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.LightGrey
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoresBottomSheet(
    viewModel: HomeViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        viewModel.loadCurrentStore()
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {

        Column(
            modifier = Modifier.padding(Dimens._16dp)
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.shop_list),
                    fontWeight = FontWeight.Bold,
                    fontSize = Dimens._18sp
                )
                VerticalSpacer(Dimens._4dp)
                Divider(color = LightGrey)
                VerticalSpacer(Dimens._4dp)
            }

            VerticalSpacer(Dimens._8dp)
            val stores = viewModel.state.userStores

            LazyColumn(
                modifier = Modifier
            ) {
                items(stores) { item ->
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

                        Row(modifier = Modifier.fillMaxWidth()
                        ) {

                            Column() {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(Dimens._16dp)
                                    ) {
                                        CircleIcon(
                                            onClick = { /*TODO*/ },
                                            containerColor = LightGrey
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Storefront,
                                                contentDescription = "store",
                                                tint = DeepSeaBlue,
                                                modifier = Modifier.size(Dimens._20dp)
                                            )
                                        }

                                        HorizontalSpacer(Dimens._8dp)

                                        Text(
                                            text = item.name,
                                            textAlign = TextAlign.Center,
                                            fontSize = Dimens._16sp,
                                        )
                                    }

                                    TextButton(onClick = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                onDismiss()
                                            }
                                        }

                                        viewModel.onEvent(HomeEvent.SetCurrentStore(storeId = item.id))
                                    }) {
                                        Text(text = "Select")
                                    }
                                }
                            }
                        }
                    }
                    VerticalSpacer(Dimens._6dp)
                }
            }

            VerticalSpacer(Dimens._50dp)
        }
    }
}