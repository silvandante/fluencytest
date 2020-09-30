package com.walker.fluencytest.data.models.auth


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("token")
    val token: String
)