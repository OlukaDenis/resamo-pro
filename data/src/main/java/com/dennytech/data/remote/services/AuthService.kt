package com.dennytech.data.remote.services

import com.dennytech.data.remote.models.SignupResponseModel
import com.dennytech.data.remote.models.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {


    @POST("user/login")
    suspend fun login(@Body request: HashMap<String, Any>): UserResponse

    @POST("user")
    suspend fun signup(@Body request: HashMap<String, Any>): SignupResponseModel


}