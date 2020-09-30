package com.walker.fluencytest.data.models.cards.dto

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class NoteDto (
    var cardId: Int = 0,
    var dateCreated: Long = 0,
    var dateUpdated: Long = 0,
    var `field`: String = "",
    var fieldName: String = "",
    var fieldNumber: Int = 0,
    @PrimaryKey var id: Int = 0,
    var noteType: Int = 0
): RealmObject()