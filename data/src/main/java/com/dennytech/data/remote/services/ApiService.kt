package com.dennytech.data.remote.services

import com.dennytech.data.remote.models.CountryResponseModel
import com.dennytech.data.remote.models.ProductsResponseModel
import com.dennytech.data.remote.models.ProvinceResponseModel
import com.dennytech.data.remote.models.RefreshTokenResponseModel
import com.dennytech.data.remote.models.StateResponseModel
import com.dennytech.data.remote.models.UserResponseModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
    ): ProductsResponseModel

    @GET("user")
    suspend fun getCurrentUser(): UserResponseModel
}