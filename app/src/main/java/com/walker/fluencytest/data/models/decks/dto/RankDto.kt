package com.walker.fluencytest.data.models.decks.dto

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class RankDto(
    var score: String = ""
): RealmObject()