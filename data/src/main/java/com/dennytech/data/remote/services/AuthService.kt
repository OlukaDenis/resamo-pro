package com.dennytech.data.remote.services

import com.dennytech.data.remote.models.RefreshTokenResponseModel
import com.dennytech.data.remote.models.SignupResponseModel
import com.dennytech.data.remote.models.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface AuthService {


    @POST("user/login")
    suspend fun login(@Body request: HashMap<String, Any>): UserResponse

    @GET("user/refreshToken")
    suspend fun refreshToken(@HeaderMap headers: HashMap<String, Any>): RefreshTokenResponseModel

    @POST("user")
    suspend fun signup(@Body request: HashMap<String, Any>): SignupResponseModel


}