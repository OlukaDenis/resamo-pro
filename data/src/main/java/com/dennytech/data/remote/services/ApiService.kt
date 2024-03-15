package com.dennytech.data.remote.services

import com.dennytech.data.remote.models.CountryResponseModel
import com.dennytech.data.remote.models.ProductListResponseModel
import com.dennytech.data.remote.models.ProductResponseModel
import com.dennytech.data.remote.models.ProvinceResponseModel
import com.dennytech.data.remote.models.RefreshTokenResponseModel
import com.dennytech.data.remote.models.StateResponseModel
import com.dennytech.data.remote.models.UserResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.QueryMap

interface ApiService {

    @GET("user/refreshToken")
    suspend fun refreshToken(): RefreshTokenResponseModel

    @GET("general/usa/states")
    suspend fun getStateList(): StateResponseModel

    @GET("general/canada/provinces")
    suspend fun getProvinceList(): ProvinceResponseModel

    @GET("general/country/all")
    suspend fun getCountryList(): CountryResponseModel

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

    @GET("user")
    suspend fun getCurrentUser(): UserResponseModel
}