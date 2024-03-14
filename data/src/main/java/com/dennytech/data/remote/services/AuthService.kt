package com.dennytech.data.remote.services

import com.dennytech.data.remote.models.CheckUsernameResponse
import com.dennytech.data.remote.models.RemoteSuccessResponse
import com.dennytech.data.remote.models.SignupResponseModel
import com.dennytech.data.remote.models.TokenResponseModel
import com.dennytech.data.remote.models.UserResponseModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface AuthService {


    @POST("user/login")
    suspend fun login(@Body request: HashMap<String, Any>): UserResponseModel




    @POST("user/otp")
    suspend fun sendToken(@Body request: HashMap<String, Any>): RemoteSuccessResponse

    @POST("user/waitlist")
    suspend fun joinWaitlist(@Body request: HashMap<String, Any>): RemoteSuccessResponse

    @POST("user")
    suspend fun signup(@Body request: HashMap<String, Any>): SignupResponseModel

    @GET("user/used")
    suspend fun checkUsername(@QueryMap request: HashMap<String, Any>): CheckUsernameResponse

}