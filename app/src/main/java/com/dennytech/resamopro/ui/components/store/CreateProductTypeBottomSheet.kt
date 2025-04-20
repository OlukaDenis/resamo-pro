package com.dennytech.resamopro.ui.components.store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.components.CloseIcon
import com.dennytech.resamopro.ui.components.CustomButton
import com.dennytech.resamopro.ui.components.CustomTextField
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.models.events.StoreDetailEvent
import com.dennytech.resamopro.ui.screen.main.stores.detail.StoreDetailViewModel
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliBlue
import com.dennytech.resamopro.ui.theme.TruliBlueLight900


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductTypeBottomSheet(
    viewModel: StoreDetailViewModel = hiltViewModel(),
    storeId: String,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { false }
    )

    ModalBottomSheet(
        onDismissRequest = { },
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
                    "Create Product Type",
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

            VerticalSpacer(Dimens._16dp)

            CustomTextField(
                value = viewModel.state.type,
                onValueChange = {
                    viewModel.onEvent(StoreDetailEvent.TypeChanged(it))
                },
                placeholder = "Product Type",
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.state.typeError.isNotEmpty(),
                errorMessage = viewModel.state.typeError,
            )

            VerticalSpacer(Dimens._30dp)

            CustomButton(title = stringResource(R.string.save),
                modifier = Modifier.weight(1f),
                loading = viewModel.state.loading,
                onClick = { viewModel.onEvent(StoreDetailEvent.SubmitType) })

            Spacer(modifier = Modifier.height(Dimens._40dp))

        }
    }
}