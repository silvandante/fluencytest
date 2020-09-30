package com.walker.fluencytest.data.models.cards.serialized


import com.google.gson.annotations.SerializedName

data class Note(
    @SerializedName("cardId")
    val cardId: Int,
    @SerializedName("dateCreated")
    val dateCreated: Long,
    @SerializedName("dateUpdated")
    val dateUpdated: Long,
    @SerializedName("field")
    val `field`: String,
    @SerializedName("fieldName")
    val fieldName: String,
    @SerializedName("fieldNumber")
    val fieldNumber: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("noteType")
    val noteType: Int
)