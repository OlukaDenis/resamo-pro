package com.dennytech.resamopro.ui.components.store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.resamopro.ui.components.HorizontalSpacer
import com.dennytech.resamopro.ui.components.LoadingCircle
import com.dennytech.resamopro.ui.components.ProfileIcon
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.screen.main.stores.detail.StoreDetailViewModel
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.LightGrey
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnAssignedStoreUsersBottomSheet(
    viewModel: StoreDetailViewModel = hiltViewModel(),
    storeId: String,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

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
                    "Assign Users",
                    fontWeight = FontWeight.Bold,
                    fontSize = Dimens._18sp
                )
                VerticalSpacer(Dimens._4dp)
                Divider(color = LightGrey)
                VerticalSpacer(Dimens._4dp)
            }

            VerticalSpacer(Dimens._8dp)
            val stores = viewModel.state.unassignedUsers

            if (viewModel.state.loading) {
                Column(modifier = Modifier.padding(vertical = Dimens._30dp)) {
                    LoadingCircle()
                }
            } else {
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

                                if (viewModel.state.unassignedUsers.isEmpty()) {
                                    Text(text = "Empty users", color = Color.Gray)
                                } else {
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
                                                ProfileIcon()

                                                HorizontalSpacer(Dimens._8dp)

                                                Text(
                                                    text = item.fullName,
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

                                            }) {
                                                Text(text = "Assign")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        VerticalSpacer(Dimens._6dp)
                    }
                }
            }

            VerticalSpacer(Dimens._50dp)
        }
    }
}