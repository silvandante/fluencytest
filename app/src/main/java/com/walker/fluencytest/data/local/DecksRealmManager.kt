package com.walker.fluencytest.data.local

import com.walker.fluencytest.data.constants.AppConstants
import com.walker.fluencytest.data.models.decks.dto.DeckDataDto
import com.walker.fluencytest.data.models.decks.serialized.DecksModel
import com.walker.fluencytest.data.network.listener.APIListener
import io.realm.Realm
import io.realm.RealmConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DecksRealmManager  {

    val realm: Realm by lazy {
        val config = RealmConfiguration.Builder()
            .name(AppConstants.SHARED_PREFERENCES.FLUENCY_DB_DECKS)
            .schemaVersion(2)
            .build()

        Realm.getInstance(config)
    }

    fun findDecks(): RealmLiveData<DeckDataDto> {
        return realm.where(DeckDataDto::class.java).findAll().asLiveData()
    }

    fun mRealmLiveData(): RealmLiveData<DeckDataDto> {
        return realm.where(DeckDataDto::class.java).findAllAsync().asLiveData()
    }

    fun hasDecks(): Boolean {
        return realm.where(DeckDataDto::class.java).findAll().size>0
    }

    fun findDeckById(deckId: Int): DeckDataDto? {
        return realm.where(DeckDataDto::class.java).equalTo("card.deckId", deckId).findFirst()
    }

    fun insertDeck(newDeck: DeckDataDto){
            realm.beginTransaction()
            if (realm.where(DeckDataDto::class.java).equalTo("deckId",newDeck.deckId).findFirst()==null) {
                realm.insertOrUpdate(newDeck)
            }
            realm.commitTransaction()
    }

}