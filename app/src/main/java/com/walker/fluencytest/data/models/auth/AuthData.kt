package com.walker.fluencytest.data.models.auth


import com.google.gson.annotations.SerializedName

data class AuthData(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)