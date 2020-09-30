package com.walker.fluencytest.data.models.cards.dto

import androidx.room.PrimaryKey
import com.walker.fluencytest.data.models.cards.serialized.Note
import com.walker.fluencytest.data.models.cards.serialized.Score
import io.reactivex.annotations.Nullable
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Required

open class CardDataDto(
    @PrimaryKey var id: Long = 0,
    var card: CardDto? = CardDto(),
    var notes: RealmList<NoteDto> = RealmList(),
    var score: ScoreDto? = ScoreDto(),
    var pathAudio: String = "",
    var originalUrlAudio: String = ""
): RealmObject() 