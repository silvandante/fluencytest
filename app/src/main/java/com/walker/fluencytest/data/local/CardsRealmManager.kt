package com.walker.fluencytest.data.local

import android.os.Environment
import android.widget.Toast
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util.toByteArray
import com.walker.fluencytest.data.constants.AppConstants
import com.walker.fluencytest.data.models.cards.dto.CardDataDto
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.InputStream


class CardsRealmManager {

    val realm: Realm by lazy {
        val config = RealmConfiguration.Builder()
            .name(AppConstants.SHARED_PREFERENCES.FLUENCY_DB_CARDS)
            .schemaVersion(2)
            .build()

        Realm.getInstance(config)
    }

    fun findCardsByDeckId(deckId: Int): List<CardDataDto> ? {
        return realm.where(CardDataDto::class.java).equalTo("card.deckId", deckId).findAll()
    }

    fun insertCard(newCard: CardDataDto){
        Log.d("inseriu_card", newCard.notes[0]?.field)
        realm.beginTransaction()
        realm.insertOrUpdate(newCard)
        realm.commitTransaction()
    }



}