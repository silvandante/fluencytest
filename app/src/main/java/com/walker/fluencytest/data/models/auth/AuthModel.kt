package com.walker.fluencytest.data.models.auth


import com.google.gson.annotations.SerializedName

data class AuthModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("meta")
    val meta: Meta
)