package com.dennytech.resamopro.ui.components.store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.dennytech.resamopro.ui.components.CloseIcon
import com.dennytech.resamopro.ui.components.HorizontalSpacer
import com.dennytech.resamopro.ui.components.LoadingCircle
import com.dennytech.resamopro.ui.components.ProfileIcon
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.models.events.StoreDetailEvent
import com.dennytech.resamopro.ui.screen.main.stores.detail.StoreDetailViewModel
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliBlue
import com.dennytech.resamopro.ui.theme.TruliBlueLight900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnAssignedStoreUsersBottomSheet(
    viewModel: StoreDetailViewModel = hiltViewModel(),
    storeId: String,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = {false}
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {  },
        sheetState = sheetState
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens._16dp)
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
            ) {

                Text(
                    "Assign Users",
                    fontWeight = FontWeight.Bold,
                    fontSize = Dimens._18sp
                )
            }

            IconButton(
                onClick = { onDismiss() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = TruliBlueLight900
                )
            ) {
                CloseIcon(tint = TruliBlue)
            }
        }


        Column(
            modifier = Modifier.padding(Dimens._16dp)
        ) {

            VerticalSpacer(Dimens._8dp)
            val users = viewModel.state.unassignedUsers

            if (viewModel.state.loading) {
                Column(modifier = Modifier.padding(vertical = Dimens._30dp)) {
                    LoadingCircle()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                ) {
                    items(users) { item ->
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
                                   Column(modifier = Modifier.padding(vertical = Dimens._30dp)) {
                                       Text(text = "Empty users", color = Color.Gray)
                                   }
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

                                            if (viewModel.state.assigningUser && viewModel.userIdSelected == item.id) {
                                                Column(
                                                    modifier = Modifier.padding(end = Dimens._8dp)
                                                ) {
                                                    CircularProgressIndicator(
                                                        modifier = Modifier.size(Dimens._24dp),
                                                        color = TruliBlue,
                                                        strokeWidth = Dimens._3dp,
                                                    )
                                                }
                                            } else {
                                                TextButton(onClick = {
                                                    viewModel.onEvent(
                                                        StoreDetailEvent.AssignUser(
                                                        userId = item.id,
                                                        storeId = storeId
                                                    ))
                                                }) {
                                                    Text(text = "Assign")
                                                }
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