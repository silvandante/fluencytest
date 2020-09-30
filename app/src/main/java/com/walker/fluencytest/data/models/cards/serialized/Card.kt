package com.walker.fluencytest.data.models.cards.serialized


import com.google.gson.annotations.SerializedName


data class Card(
    @SerializedName("cardType")
    val cardType: Int,
    @SerializedName("dateCreated")
    val dateCreated: Long,
    @SerializedName("dateUpdated")
    val dateUpdated: Long,
    @SerializedName("deckId")
    val deckId: Int,
    @SerializedName("id")
    val id: Int
)