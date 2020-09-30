package com.walker.fluencytest.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.walker.fluencytest.FluenyTestApplication
import com.walker.fluencytest.data.local.DecksRealmManager
import com.walker.fluencytest.data.local.asLiveData
import com.walker.fluencytest.data.models.decks.dto.DeckDataDto
import com.walker.fluencytest.data.network.listener.APIListener
import com.walker.fluencytest.data.models.decks.serialized.Data
import com.walker.fluencytest.data.models.decks.serialized.DecksModel
import com.walker.fluencytest.data.network.repositories.DeckRepository
import io.realm.Realm
import javax.inject.Inject

class FeedDeckViewModel (application: Application) : AndroidViewModel(application) {


    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    var decksRealmManager: DecksRealmManager = DecksRealmManager()

    var decks: LiveData<List<DeckDataDto>> = Transformations.map(decksRealmManager.mRealmLiveData()) {
        realmResult ->
        realm.copyFromRealm(realmResult)
    }

    val hasDecks: Boolean = decksRealmManager.hasDecks()

    fun insertDeck (newDeck: DeckDataDto) {
        decksRealmManager.insertDeck(newDeck)
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }
}