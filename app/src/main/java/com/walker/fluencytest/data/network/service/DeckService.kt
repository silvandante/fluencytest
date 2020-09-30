package com.walker.fluencytest.data.network.service

import com.walker.fluencytest.data.models.decks.serialized.DecksModel
import com.walker.fluencytest.data.models.cards.serialized.CardModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DeckService {

    @GET("v3/users/{user_id}/decks")
    fun getDecks(@Path("user_id") user_id: String): Call<DecksModel>


    @GET("v2/decks/{deck_id}/cards")
    fun getCards(@Path("deck_id") deck_id: String): Call<CardModel>
}