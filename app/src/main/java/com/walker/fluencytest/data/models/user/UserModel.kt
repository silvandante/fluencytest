package com.walker.fluencytest.data.models.user


import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("meta")
    val meta: Meta
)