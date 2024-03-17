package com.dennytech.data.impl

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dennytech.data.remote.datasource.ProductPagingSource
import com.dennytech.data.remote.models.RemoteProductModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.data.utils.MAX_PAGE_SIZE
import com.dennytech.data.utils.readAsRequestBody
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.repository.ProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) : ProductRepository {
    override suspend fun fetchProducts(filters: HashMap<String, Any>): Flow<PagingData<ProductDomainModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = MAX_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                ProductPagingSource(apiService, filters)
            }
        ).flow
    }

    override suspend fun fetchRecentProducts(): List<ProductDomainModel> {
        return try {
            val request = HashMap<String, Any>().apply {
                this["page"] = 1
                this["pageSize"] = 5
            }

            val filter = HashMap<String, Any>()

            val response = apiService.getProducts(request, filter)
            response.data.map { it.toDomain() }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun createNewProduct(
        fileUri: Uri,
        fields: HashMap<String, String>
    ): ProductDomainModel {

        return try {

            context.contentResolver.getType(fileUri)?.let { mimeType ->
                val file = File(fileUri.path!!)
                val requestBody = context.contentResolver.readAsRequestBody(fileUri)
                val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)

                val fieldsMap = HashMap<String, RequestBody?>().apply {
                    put("name", fields["name"]?.toRequestBody("*/*".toMediaTypeOrNull()))
                    put("price", fields["price"]?.toRequestBody("*/*".toMediaTypeOrNull()))
                    put("color", fields["color"]?.toRequestBody("*/*".toMediaTypeOrNull()))
                    put("size", fields["size"]?.toRequestBody("*/*".toMediaTypeOrNull()))
                    put("type", fields["type"]?.toRequestBody("*/*".toMediaTypeOrNull()))
                    put("brand", fields["brand"]?.toRequestBody("*/*".toMediaTypeOrNull()))
                }

                apiService.createProduct(filePart, fieldsMap).data.toDomain()
            } ?: throw Exception("Error occured")


        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun updateProduct(
        productId: String,
        fileUri: Uri?,
        fields: HashMap<String, String?>
    ): ProductDomainModel {

            return try {

                val fieldsMap = HashMap<String, RequestBody?>().apply {
                    put("name", fields["name"]?.toRequestBody("*/*".toMediaTypeOrNull()))
                    put("price", fields["price"]?.toRequestBody("*/*".toMediaTypeOrNull()))
                    put("color", fields["color"]?.toRequestBody("*/*".toMediaTypeOrNull()))
                    put("size", fields["size"]?.toRequestBody("*/*".toMediaTypeOrNull()))
                    put("type", fields["type"]?.toRequestBody("*/*".toMediaTypeOrNull()))
                    put("brand", fields["brand"]?.toRequestBody("*/*".toMediaTypeOrNull()))
                }


                if (fileUri != null) {
                    val file = File(fileUri.path!!)
                    val requestBody = context.contentResolver.readAsRequestBody(fileUri)
                    val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)

                    apiService.updateProductWithFile(productId, filePart, fieldsMap).data.toDomain()
                } else {
                    apiService.updateProduct(productId, fieldsMap).data.toDomain()
                }

            } catch (throwable: Throwable) {
                throw throwable
            }

    }

    override suspend fun createProductSale(request: HashMap<String, Any>): Int {
        return try {

            val response = apiService.createSale(request)
            response.statusCode
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}