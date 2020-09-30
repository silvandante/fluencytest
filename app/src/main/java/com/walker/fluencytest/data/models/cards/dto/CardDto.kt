package com.walker.fluencytest.data.models.cards.dto

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CardDto (
    var cardType: Int = 0,
    var dateCreated: Long = 0,
    var dateUpdated: Long = 0,
    var deckId: Int = 0,
    @PrimaryKey var id: Int = 0
): RealmObject()