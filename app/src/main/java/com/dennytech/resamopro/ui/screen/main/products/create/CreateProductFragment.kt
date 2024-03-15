package com.dennytech.resamopro.ui.screen.main.products.create

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.core.net.toFile
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.components.AddCircleIcon
import com.dennytech.resamopro.ui.components.CustomButton
import com.dennytech.resamopro.ui.components.CustomExposedDropdown
import com.dennytech.resamopro.ui.components.CustomTextField
import com.dennytech.resamopro.ui.components.ErrorLabel
import com.dennytech.resamopro.ui.screen.auth.login.LoginEvent
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.utils.Helpers
import com.dennytech.resamopro.utils.Helpers.toMegaBytes
import timber.log.Timber
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductFragment(
    viewModel: CreateProductViewModel = hiltViewModel(),
    navigateUp: () -> Unit
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
                        text = stringResource(R.string.new_product),
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

        if (viewModel.uploadComplete) {
            viewModel.onEvent(CreateProductEvent.Reset)
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            ImagePicker()

            if (viewModel.state.imageError.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(Dimens._16dp))
                    ErrorLabel(message = viewModel.state.imageError)
                }
            }

            Column(
                modifier = Modifier.padding(Dimens._16dp)
            ) {
                CustomTextField(
                    value = viewModel.state.name,
                    onValueChange = { viewModel.onEvent(CreateProductEvent.NameChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = stringResource(R.string.product_name),
                    isError = viewModel.state.nameError.isNotEmpty(),
                    errorMessage = viewModel.state.nameError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                CustomTextField(
                    value = viewModel.state.size,
                    onValueChange = { viewModel.onEvent(CreateProductEvent.SizeChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Size",
                    isError = viewModel.state.sizeError.isNotEmpty(),
                    errorMessage = viewModel.state.sizeError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                CustomTextField(
                    value = viewModel.state.brand,
                    onValueChange = { viewModel.onEvent(CreateProductEvent.BrandChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Brand",
                    isError = viewModel.state.brandError.isNotEmpty(),
                    errorMessage = viewModel.state.brandError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                CustomTextField(
                    value = viewModel.state.color,
                    onValueChange = { viewModel.onEvent(CreateProductEvent.ColorChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Color",
                    isError = viewModel.state.colorError.isNotEmpty(),
                    errorMessage = viewModel.state.colorError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                CustomTextField(
                    value = viewModel.state.price,
                    onValueChange = { viewModel.onEvent(CreateProductEvent.PriceChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Buying Price",
                    isError = viewModel.state.priceError.isNotEmpty(),
                    errorMessage = viewModel.state.priceError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                CustomExposedDropdown(
                    placeholder = stringResource(R.string.product_type),
                    onValueChange = { viewModel.onEvent(CreateProductEvent.TypeChanged(it)) },
                    items = Helpers.shoeTypes(),
                    modifier = Modifier.fillMaxWidth(),
                    errorMessage = viewModel.state.typeError
                )

                if (viewModel.state.error.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(Dimens._16dp))
                        ErrorLabel(message = viewModel.state.error)
                    }
                }

                Spacer(modifier = Modifier.height(Dimens._30dp))

                CustomButton(title = stringResource(R.string.save),
                    modifier = Modifier.weight(1f),
                    loading = viewModel.state.loading,
                    onClick = { viewModel.onEvent(CreateProductEvent.Submit) })

                Spacer(modifier = Modifier.height(Dimens._20dp))
            }

        }
    }
}

@Composable
private fun ImagePicker(viewModel: CreateProductViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

            val file = createTempFile()
            uri?.let { context.contentResolver.openInputStream(it) }.use { input ->
                file.outputStream().use { output ->
                    input?.copyTo(output)
                }

                if (file.length().toMegaBytes() < 5) {
                    viewModel.onEvent(CreateProductEvent.SetImageFile(uri))
                    file.delete() // Delete file to avoid flooding cache storage
                } else {
                    viewModel.state = viewModel.state.copy(imageError = "Image is too big")
                }
            }
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens._14dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(Dimens._8dp),
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens._14dp)
                .fillMaxWidth()
                .height(Dimens._200dp)
                .clickable {
                    launcher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            when (viewModel.state.imageUri) {
                null -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        AddCircleIcon()
                        Spacer(modifier = Modifier.width(Dimens._8dp))
                        Text(text = "Add Image")
                    }
                }
                else -> {
                    val painter = rememberAsyncImagePainter(
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(data = viewModel.state.imageUri)
                            .build()
                    )

                    Image(
                        painter = painter,
                        contentDescription = "image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }

        }
    }
}