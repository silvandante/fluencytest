package com.walker.fluencytest.ui.card

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.walker.fluencytest.data.local.CardsRealmManager
import com.walker.fluencytest.data.local.DecksRealmManager
import com.walker.fluencytest.data.models.cards.dto.CardDataDto
import com.walker.fluencytest.data.network.listener.APIListener
import com.walker.fluencytest.data.models.cards.serialized.CardModel
import com.walker.fluencytest.data.models.cards.serialized.Data
import com.walker.fluencytest.data.models.decks.dto.DeckDataDto
import com.walker.fluencytest.data.network.repositories.DeckRepository
import io.realm.Realm

class CardsModelView (application: Application) : AndroidViewModel(application) {

    private val decksRepository = DeckRepository()
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    var cardsRealmManager: CardsRealmManager = CardsRealmManager()

    private val mCards = MutableLiveData<List<Data>>()
    val cards: LiveData<List<Data>> = mCards

    fun insertCard (newCard: CardDataDto) {
        cardsRealmManager.insertCard(newCard)
    }

    /**
     * Pega cars de um deck do usuário
     */

    fun getCards(deckId: String): List<CardDataDto>? {
        return cardsRealmManager.findCardsByDeckId(deckId.toInt())
    }

    fun getCardsFromNetwork(deckId: String) {
        decksRepository.getCards(deckId,object : APIListener<CardModel> {
            override fun onSuccess(model: CardModel) {
                mCards.value = model.data
            }

            override fun onError(str: String) {
                mCards.value = null
            }

            override fun onFailure(model: CardModel) {
                mCards.value = null
            }

        })
    }

}