package com.walker.fluencytest.data.models.decks.serialized


import com.google.gson.annotations.SerializedName

data class Rank(
    @SerializedName("score")
    val score: String
)