package com.dennytech.data.remote.services

import com.dennytech.data.remote.models.CreateSaleResponseModel
import com.dennytech.data.remote.models.ProductListResponseModel
import com.dennytech.data.remote.models.ProductResponseModel
import com.dennytech.data.remote.models.RefreshTokenResponseModel
import com.dennytech.data.remote.models.UserListResponse
import com.dennytech.data.remote.models.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {

    @GET("user/refreshToken")
    suspend fun refreshToken(): RefreshTokenResponseModel

    @POST("product/list")
    suspend fun getProducts(
        @QueryMap request: HashMap<String, Any>,
        @Body body: HashMap<String, Any>
    ): ProductListResponseModel

    @Multipart
    @POST("product")
    suspend fun createProduct(
        @Part filePart: MultipartBody.Part,
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody?>
    ): ProductResponseModel

    @Multipart
    @PATCH("product/{id}")
    suspend fun updateProductWithFile(
        @Path("id") id: String,
        @Part filePart: MultipartBody.Part,
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody?>
    ): ProductResponseModel

    @Multipart
    @PATCH("product/{id}")
    suspend fun updateProduct(
        @Path("id") id: String,
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody?>
    ): ProductResponseModel


    @GET("user/list")
    suspend fun getUsers(
        @QueryMap request: HashMap<String, Any>
    ): UserListResponse

    @GET("user/activate/{id}")
    suspend fun activateUser(@Path("id") id: String): UserResponse

    @GET("user/deactivate/{id}")
    suspend fun deactivateUser(@Path("id") id: String): UserResponse

    @POST("sale")
    suspend fun createSale(@Body body: HashMap<String, Any>): CreateSaleResponseModel

    @POST("user")
    suspend fun createUser(@Body body: HashMap<String, Any>): UserResponse

    @GET("user")
    suspend fun getCurrentUser(): UserResponse
}