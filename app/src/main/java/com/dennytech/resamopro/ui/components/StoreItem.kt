package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.resamopro.ui.screen.main.home.HomeEvent
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.LightGrey
import kotlinx.coroutines.launch

@Composable
fun StoreItem(
    store: StoreDomainModel,
    onMenuClick: (String) -> Unit,
    onClick: () -> Unit
) {

    val dropdownList = mutableListOf<String>().apply {
        this.add("View")
        this.add("Assign")
    }

    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens._0dp
        ),
        shape = RoundedCornerShape(Dimens._16dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens._4dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }
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
                            onClick = { },
                            containerColor = LightGrey
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Storefront,
                                contentDescription = "Keyboard down",
                                tint = DeepSeaBlue,
                                modifier = Modifier.size(Dimens._20dp)
                            )
                        }

                        HorizontalSpacer(Dimens._8dp)

                        Column {
                            Text(
                                text = store.name,
                                fontSize = Dimens._16sp,
                            )
                            Text(
                                text = store.location,
                                fontSize = Dimens._14sp,
                                color = Color.Gray
                            )
                        }
                    }

                    IconButton(onClick = {
//                        isContextMenuVisible = true
                    }) {
                        RightKeyboardArrowIcon()
                    }

                    DropdownMenu(
                        expanded = isContextMenuVisible,
                        onDismissRequest = { isContextMenuVisible = false }
                    ) {

                        dropdownList.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    onMenuClick(item)
                                    isContextMenuVisible = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}