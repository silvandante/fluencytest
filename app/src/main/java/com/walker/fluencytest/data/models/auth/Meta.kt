package com.walker.fluencytest.data.models.auth


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("code")
    val code: Int
)