package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.dennytech.resamopro.utils.Helpers.millisecondsToDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDatePicker(
    showDatePicker: Boolean = false,
    toggleCalender: (Boolean) -> Unit,
    onDateChanged: (String) -> Unit,
    title: String = "Select Date"
) {

    val state = rememberDatePickerState()
    if (showDatePicker) {
        Column {
            DatePickerDialog(
                onDismissRequest = {
                    toggleCalender(false)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            toggleCalender(false)

                            state.selectedDateMillis?.let { millis ->
                                onDateChanged(millisecondsToDate(millis))
                            }
                        }
                    ) {
                        Text("Okay")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            toggleCalender(false)
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = state,
                    title = {
                        Text(text = title)
                    }
                )
            }
        }
    }
}