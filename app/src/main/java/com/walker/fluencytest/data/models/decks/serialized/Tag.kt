package com.walker.fluencytest.data.models.decks.serialized


import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("deckId")
    val deckId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("tag")
    val tag: String
)