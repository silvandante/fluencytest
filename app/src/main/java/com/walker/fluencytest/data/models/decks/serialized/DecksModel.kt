package com.walker.fluencytest.data.models.decks.serialized


import com.google.gson.annotations.SerializedName

data class DecksModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("meta")
    val meta: Meta
)