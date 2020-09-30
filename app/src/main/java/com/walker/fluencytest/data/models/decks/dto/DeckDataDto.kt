package com.walker.fluencytest.data.models.decks.dto

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DeckDataDto (
    var authorId: Int = 0,
    var cardCount: Int = 0,
    var cardsPerSession: Int = 0,
    @PrimaryKey var deckId: Int = 0,
    var isPublic: Boolean = true,
    var kind: Int = 0,
    var lastLearned: Int = 0,
    var nonOverdue: Int = 0,
    var rank: RankDto? = RankDto(),
    var tags: RealmList<TagDto> = RealmList(),
    var title: String = "",
    var virtualId: Int = 0
): RealmObject() {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}