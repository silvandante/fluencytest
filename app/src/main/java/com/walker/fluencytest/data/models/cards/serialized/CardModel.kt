package com.walker.fluencytest.data.models.cards.serialized


import com.google.gson.annotations.SerializedName

data class CardModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("meta")
    val meta: Meta
)