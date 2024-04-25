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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.AddCircleIcon
import com.dennytech.resamopro.ui.components.CustomButton
import com.dennytech.resamopro.ui.components.CustomExposedDropdown
import com.dennytech.resamopro.ui.components.CustomTextField
import com.dennytech.resamopro.ui.components.ErrorLabel
import com.dennytech.resamopro.ui.components.dialogs.SuccessDialog
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliBlueLight900
import com.dennytech.resamopro.utils.Helpers.productTypeValue
import com.dennytech.resamopro.utils.Helpers.toMegaBytes
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductFragment(
    viewModel: CreateProductViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    isUpdate: Boolean = false,
    product: ProductDomainModel? = null,
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
                        text = if (isUpdate) stringResource(R.string.update_product) else stringResource(
                            R.string.new_product
                        ),
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

        viewModel.isUpdate = isUpdate

        product?.let {
            viewModel.selectedProductType = it.type
        }

        if (viewModel.state.showSuccessDialog) {
            SuccessDialog(
                dismissDialog = {
                    if (isUpdate)
                        viewModel.state = viewModel.state.copy(showSuccessDialog = false)
                    else viewModel.onEvent(CreateProductEvent.ToggleSuccessDialog)
                },
                message = if (isUpdate) "Successfully updated product" else "Successfully created product"
            )
        }

        LaunchedEffect(Unit) {
            if (isUpdate) {
                viewModel.onEvent(
                    CreateProductEvent.SetProductState(
                        CreateProductState(
                            productId = product?.id.orEmpty(),
                            name = product?.name?.replace("+", " ").orEmpty(),
                            brand = product?.brand.orEmpty(),
                            size = product?.size.toString(),
                            color = product?.color.orEmpty(),
                            price = product?.price.toString(),
                            type = product?.type.orEmpty(),
                        )
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            ImagePicker(product = product)

            if (viewModel.state.imageError.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VerticalSpacer(Dimens._10dp)
                    ErrorLabel(message = viewModel.state.imageError)
                }
            }

            Column(
                modifier = Modifier.padding(Dimens._16dp)
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = Dimens._0dp
                    ),
                    shape = RoundedCornerShape(Dimens._16dp),
                    colors = CardDefaults.cardColors(
                        containerColor = TruliBlueLight900,
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(modifier = Modifier.padding(Dimens._12dp)) {
                        Text(
                            text = "Adding new product to ${mainViewModel.state.currentStore?.name} store",
                            color = DeepSeaBlue
                        )
                    }
                }
                VerticalSpacer(Dimens._16dp)

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
                VerticalSpacer(Dimens._10dp)

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
                VerticalSpacer(Dimens._10dp)

                CustomExposedDropdown(
                    placeholder = stringResource(R.string.product_type),
                    selectedValue = viewModel.selectedProductType.productTypeValue(),
                    onValueChange = { viewModel.onEvent(CreateProductEvent.TypeChanged(it)) },
                    items = mainViewModel.state.productTypes,
                    modifier = Modifier.fillMaxWidth(),
                    errorMessage = viewModel.state.typeError
                )
                VerticalSpacer(Dimens._10dp)

                CustomExposedDropdown(
                    placeholder = stringResource(R.string.category),
                    selectedValue = viewModel.state.category?.value.orEmpty(),
                    onValueChange = {value ->
                        Timber.d("Category selected: %s", value)
                        val model = mainViewModel.state.productCategories.find { it.key == value}
                        model?.let {
                            viewModel.onEvent(CreateProductEvent.CategoryChanged(it))
                        }
                    },
                    items = mainViewModel.state.productCategories,
                    modifier = Modifier.fillMaxWidth(),
                    errorMessage = viewModel.state.typeError
                )
                VerticalSpacer(Dimens._10dp)
                CustomTextField(
                    value = viewModel.state.quantity,
                    onValueChange = { viewModel.onEvent(CreateProductEvent.QuantityChanged(it)) },
                    placeholder = "Quantity",
                    modifier = Modifier.fillMaxWidth(),
                    isError = viewModel.state.quantityError.isNotEmpty(),
                    errorMessage = viewModel.state.quantityError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                )

                VerticalSpacer(Dimens._10dp)
                CustomTextField(
                    value = viewModel.state.size,
                    onValueChange = { viewModel.onEvent(CreateProductEvent.SizeChanged(it)) },
                    placeholder = "Size",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                )
                VerticalSpacer(Dimens._10dp)
                CustomTextField(
                    value = viewModel.state.color,
                    onValueChange = { viewModel.onEvent(CreateProductEvent.ColorChanged(it)) },
                    placeholder = "Color",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                )

                VerticalSpacer(Dimens._10dp)

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
                VerticalSpacer(Dimens._10dp)


                if (viewModel.state.error.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        VerticalSpacer(Dimens._10dp)
                        ErrorLabel(message = viewModel.state.error)
                    }
                }

                Spacer(modifier = Modifier.height(Dimens._30dp))

                CustomButton(title = if (isUpdate) stringResource(R.string.update) else stringResource(
                    R.string.save
                ),
                    modifier = Modifier.weight(1f),
                    loading = viewModel.state.loading,
                    onClick = { viewModel.onEvent(CreateProductEvent.Submit) })

                Spacer(modifier = Modifier.height(Dimens._20dp))
            }

        }
    }
}

@Composable
private fun ImagePicker(
    viewModel: CreateProductViewModel = hiltViewModel(),
    product: ProductDomainModel? = null
) {
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

            when {
                viewModel.state.imageUri == null -> {
                    if (product == null) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            AddCircleIcon()
                            Spacer(modifier = Modifier.width(Dimens._8dp))
                            Text(text = "Add Image")
                        }
                    } else {
                        SubcomposeAsyncImage(
                            model = product.thumbnail,
                            contentDescription = "product image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(size = Dimens._8dp))
                                .height(Dimens._200dp),
                        ) {
                            val state = painter.state
                            if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(Dimens._50dp),
                                    contentScale = ContentScale.Crop,
                                    painter = painterResource(id = R.drawable.ic_gray_bg),
                                    contentDescription = "avatar"
                                )
                            } else {
                                SubcomposeAsyncImageContent()
                            }
                        }
                    }

                }

                viewModel.state.imageUri != null -> {
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