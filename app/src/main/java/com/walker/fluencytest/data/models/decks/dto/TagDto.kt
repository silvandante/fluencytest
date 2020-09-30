package com.walker.fluencytest.data.models.decks.dto

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TagDto(
    var deckId: Int = 0,
    @PrimaryKey var id: Int = 0,
    var tag: String = ""
): RealmObject()