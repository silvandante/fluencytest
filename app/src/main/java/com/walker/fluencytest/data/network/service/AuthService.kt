package com.walker.fluencytest.data.network.service

import com.walker.fluencytest.data.models.auth.AuthData
import com.walker.fluencytest.data.models.auth.AuthModel
import com.walker.fluencytest.data.models.user.UserModel
import retrofit2.Call
import retrofit2.http.*

interface AuthService {

    @Headers("Content-Type: application/json")
    @POST("v3/auth")
    fun login(@Body authData: AuthData) : Call<AuthModel>

    @GET("v3/auth")
    fun getUser(@Header("x-hackit-auth") token: String): Call<UserModel>
}