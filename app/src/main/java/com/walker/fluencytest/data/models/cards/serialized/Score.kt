package com.walker.fluencytest.data.models.cards.serialized


import com.google.gson.annotations.SerializedName

data class Score(
    @SerializedName("cardId")
    val cardId: Int,
    @SerializedName("cca")
    val cca: Int,
    @SerializedName("dateCreated")
    val dateCreated: Long,
    @SerializedName("dateUpdated")
    val dateUpdated: Long,
    @SerializedName("due")
    val due: Long,
    @SerializedName("easiness")
    val easiness: Double,
    @SerializedName("reviews")
    val reviews: Int
)