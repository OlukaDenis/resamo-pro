package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliBlue
import java.util.Calendar

@Composable
fun DateFilter(
    startDate: String,
    endDate: String,
    startDateChange: (String) -> Unit,
    endDateChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column {
        var showStartDatePicker by remember {
            mutableStateOf(false)
        }

        var showEndDatePicker by remember {
            mutableStateOf(false)
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {

            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(text = "Start Date", color = DeepSeaBlue,
                    fontSize = Dimens._12sp)
                VerticalSpacer(Dimens._4dp)
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = Dimens._0dp
                    ),
                    shape = RoundedCornerShape(Dimens._8dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                ) {
                    Column(
                        modifier = Modifier.clickable {
                            showStartDatePicker = !showStartDatePicker
                        },
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = Dimens._10dp, vertical = Dimens._16dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CalendarIcon(tint = TruliBlue, modifier = Modifier.size(Dimens._20dp))
                            HorizontalSpacer(Dimens._4dp)
                            Text(
                                text = startDate.ifEmpty { "YYYY-MM-DD" },
                                fontSize = Dimens._12sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
            HorizontalSpacer(Dimens._8dp)

            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(
                    text = "End Date", color = DeepSeaBlue,
                    fontSize = Dimens._12sp
                )
                VerticalSpacer(Dimens._4dp)
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = Dimens._0dp
                    ),
                    shape = RoundedCornerShape(Dimens._8dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                ) {
                    Column(
                        modifier = Modifier.clickable {
                            showEndDatePicker = !showEndDatePicker
                        },
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = Dimens._10dp, vertical = Dimens._16dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CalendarIcon(tint = TruliBlue, modifier = Modifier.size(Dimens._20dp))
                            HorizontalSpacer(Dimens._4dp)
                            Text(
                                text = endDate.ifEmpty { "YYYY-MM-DD" },
                                fontSize = Dimens._12sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            HorizontalSpacer(Dimens._8dp)
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = Dimens._0dp
                ),
                shape = RoundedCornerShape(Dimens._8dp),
                colors = CardDefaults.cardColors(
                    containerColor = TruliBlue,
                ),
                modifier = Modifier
                    .width(Dimens._60dp)
                    .padding(bottom = Dimens._4dp)
                    .clickable { onSubmit() },
            ) {
                Row(
                    modifier = Modifier
                        .padding(Dimens._10dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RightArrowIcon(tint = Color.White)
                }
            }
        }

        if (showStartDatePicker) {
            CalendarDatePicker(
                showDatePicker = showStartDatePicker,
                toggleCalender = {showStartDatePicker = !showStartDatePicker},
                onDateChanged = {startDateChange(it)}
            )
        }

        if (showEndDatePicker) {
            CalendarDatePicker(
                showDatePicker = showEndDatePicker,
                toggleCalender = {showEndDatePicker = !showEndDatePicker},
                onDateChanged = {endDateChange(it)}
            )
        }
    }
}