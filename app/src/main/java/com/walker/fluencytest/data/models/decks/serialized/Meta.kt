package com.walker.fluencytest.data.models.decks.serialized


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("code")
    val code: Int
)