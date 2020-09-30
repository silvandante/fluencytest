package com.walker.fluencytest.data.models.cards.serialized


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("card")
    val card: Card,
    @SerializedName("notes")
    val notes: List<Note>,
    @SerializedName("score")
    val score: Score
) {
    var isFront = true
}