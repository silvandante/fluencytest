package com.walker.fluencytest.data.models.decks.serialized


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("authorId")
    val authorId: Int,
    @SerializedName("cardCount")
    val cardCount: Int,
    @SerializedName("cardsPerSession")
    val cardsPerSession: Int,
    @SerializedName("deckId")
    val deckId: Int,
    @SerializedName("isPublic")
    val isPublic: Boolean,
    @SerializedName("kind")
    val kind: Int,
    @SerializedName("lastLearned")
    val lastLearned: Int,
    @SerializedName("nonOverdue")
    val nonOverdue: Int,
    @SerializedName("rank")
    val rank: Rank,
    @SerializedName("tags")
    val tags: List<Tag>,
    @SerializedName("title")
    val title: String,
    @SerializedName("virtualId")
    val virtualId: Int
)