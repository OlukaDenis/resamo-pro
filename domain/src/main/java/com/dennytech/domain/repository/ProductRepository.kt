package com.dennytech.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.dennytech.domain.models.ProductDomainModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun fetchProducts(filters: HashMap<String, Any>): Flow<PagingData<ProductDomainModel>>
    suspend fun fetchRecentProducts(): List<ProductDomainModel>
    suspend fun createNewProduct(
        fileUri: Uri,
        fields: HashMap<String, String>
    ): ProductDomainModel

    suspend fun updateProduct(
        productId: String,
        fileUri: Uri?,
        fields: HashMap<String, String?>
    ): ProductDomainModel

    suspend fun createProductSale(request: HashMap<String, Any>): Int

    suspend fun createProductType(storeId: String, request: HashMap<String, Any>): String

    suspend fun createProductCategory(request: HashMap<String, Any>): String
}