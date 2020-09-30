package com.walker.fluencytest.data.network.repositories

import com.walker.fluencytest.data.network.listener.APIListener
import com.walker.fluencytest.data.models.decks.serialized.DecksModel
import com.walker.fluencytest.data.network.service.DeckService
import com.walker.fluencytest.data.models.cards.serialized.CardModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeckRepository {

    private val remote = RetrofitClient.createService(DeckService::class.java)

    fun getDecks(user_id: String, listenerDecks: APIListener<DecksModel>){
        val call: Call<DecksModel> = remote.getDecks(user_id)
        call.enqueue(object: Callback<DecksModel>{
            override fun onResponse(call: Call<DecksModel>, response: Response<DecksModel>) {
                if (response.body()?.meta?.code==200)
                    response.body()?.let { listenerDecks.onSuccess(it) }
                else
                    response.body()?.let { listenerDecks.onFailure(it) }

            }

            override fun onFailure(call: Call<DecksModel>, t: Throwable) {
                listenerDecks.onError((t.message.toString()))
            }

        })
    }

    fun getCards(deck_id: String, listenerCards: APIListener<CardModel>){
        val call: Call<CardModel> = remote.getCards(deck_id)
        call.enqueue(object: Callback<CardModel>{
            override fun onResponse(call: Call<CardModel>, response: Response<CardModel>) {
                if (response.body()?.meta?.code==200)
                    response.body()?.let { listenerCards.onSuccess(it) }
                else
                    response.body()?.let { listenerCards.onFailure(it) }

            }

            override fun onFailure(call: Call<CardModel>, t: Throwable) {
                listenerCards.onError((t.message.toString()))
            }

        })
    }

}