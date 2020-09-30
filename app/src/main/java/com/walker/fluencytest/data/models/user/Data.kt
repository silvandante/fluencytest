package com.walker.fluencytest.data.models.user


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("user")
    val user: UserX
)