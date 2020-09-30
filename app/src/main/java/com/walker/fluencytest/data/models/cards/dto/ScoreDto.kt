package com.walker.fluencytest.data.models.cards.dto

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ScoreDto (
    @PrimaryKey var idScore: Int = 0,
    var cardId: Int = 0,
    var cca: Int = 0,
    var dateCreated: Long = 0,
    var dateUpdated: Long = 0,
    var due: Long = 0,
    var easiness: Double = 0.0,
    var reviews: Int = 0
) : RealmObject()