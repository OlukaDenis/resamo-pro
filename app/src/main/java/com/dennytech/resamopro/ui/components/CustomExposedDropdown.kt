package com.dennytech.resamopro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dennytech.resamopro.models.KeyValueModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomExposedDropdown(
    modifier: Modifier = Modifier,
    placeholder: String,
    selectedValue: String? = null,
    items: List<KeyValueModel>,
    onValueChange: (String) -> Unit,
    errorMessage: String = "",
) {
    val first = when {
        placeholder.isEmpty() && items.isEmpty() -> ""
        placeholder.isEmpty() && items.isNotEmpty() -> items[0].value
        else -> ""
    }

    LaunchedEffect(Unit) {
        onValueChange(selectedValue ?: first)
    }

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var value by remember {
        mutableStateOf(selectedValue ?: first)
    }

    Column {
        ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = isExpanded,
            onExpandedChange = { newValue ->
                isExpanded = newValue
            }
        ) {
            CustomTextField(
                value = value,
                onValueChange = {onValueChange(it)},
                readOnly = true,
                isError = errorMessage.isNotEmpty(),
                errorMessage = errorMessage,
                placeholder = placeholder,
                modifier = modifier.menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                modifier = Modifier,
                onDismissRequest = {
                    isExpanded = false
                }
            ) {
                items.map { item ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        text = {
                            Text(text = item.value)
                        },
                        onClick = {
                            value = item.value
                            onValueChange(item.key)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}