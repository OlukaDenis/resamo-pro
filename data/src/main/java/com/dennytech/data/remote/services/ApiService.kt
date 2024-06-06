package com.dennytech.data.remote.services

import com.dennytech.data.remote.models.ConfirmSaleResponse
import com.dennytech.data.remote.models.CreateSaleResponseModel
import com.dennytech.data.remote.models.GenericUserResponse
import com.dennytech.data.remote.models.ProductListResponseModel
import com.dennytech.data.remote.models.ProductResponseModel
import com.dennytech.data.remote.models.RefreshTokenResponseModel
import com.dennytech.data.remote.models.RemoteRevenueResponse
import com.dennytech.data.remote.models.RemoteSaleCountsResponse
import com.dennytech.data.remote.models.RemoteSaleListResponse
import com.dennytech.data.remote.models.ReportModelResponse
import com.dennytech.data.remote.models.SaleReportModelResponse
import com.dennytech.data.remote.models.StoreResponseModel
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

    @POST("user")
    suspend fun createUser(@Body body: HashMap<String, Any>): UserResponse

    @GET("user/me")
    suspend fun getCurrentUser(): UserResponse

    @GET("user/me/userList")
    suspend fun getMyCreatedUsers(): UserResponse

    @GET("user/people/unassigned")
    suspend fun getUnassignedUsers(
        @QueryMap request: HashMap<String, Any>
    ): UserListResponse

    @GET("user/store/people")
    suspend fun getStoreUsers(): UserListResponse

    @GET("store/assign/{id}")
    suspend fun assignUserToStore(
        @Path("id") id: String,
        @QueryMap request: HashMap<String, Any>
    ): GenericUserResponse

    @GET("store/{id}/productType/new")
    suspend fun createProductType(
        @Path("id") storeId: String,
        @QueryMap request: HashMap<String, Any>
    ): GenericUserResponse

    @POST("sale")
    suspend fun createSale(@Body body: HashMap<String, Any>): CreateSaleResponseModel

    @GET("sale/list")
    suspend fun fetchSales(@QueryMap request: HashMap<String, Any>): RemoteSaleListResponse

    @GET("sale/counts")
    suspend fun fetchSaleCounts(@QueryMap request: HashMap<String, Any>): RemoteSaleCountsResponse

    @GET("sale/revenue")
    suspend fun fetchRevenue(@QueryMap request: HashMap<String, Any>): RemoteRevenueResponse

    @GET("sale/confirm/{id}")
    suspend fun confirmSale(@Path("id") id: String): ConfirmSaleResponse


    @POST("store")
    suspend fun createStore(@Body body: HashMap<String, Any>): StoreResponseModel

    @POST("category")
    suspend fun createCategory(@Body body: HashMap<String, Any>): GenericUserResponse

    @GET("report/salesByPeriod")
    suspend fun getSalesByPeriod(): SaleReportModelResponse

    @GET("report/popular/types")
    suspend fun getPopularProductTypes(): ReportModelResponse
}