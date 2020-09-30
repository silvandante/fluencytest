package com.walker.fluencytest.data.constants

/**
 * Constantes usadas na aplicação (enumerações)
 */
class AppConstants private constructor(){
    object SHARED_PREFERENCES {
        const val TOKEN_KEY = "@fluencytest:token"
        const val USER_ID = "@fluencytest:user_id"
        const val USER_NAME = "@fluencytest:user_name"
        const val HEADER_TOKEN = "x-hackit-auth"
        const val DECK_TAG = "@fluencytest:deck_tag"
        const val DECK_TITLE = "@fluencytest:deck_title"
        const val DECK_TAGS = "@fluencytest:deck_tags"
        const val FLUENCY_DB_DECKS = "@fluencytest:fluency_db_decks"
        const val FLUENCY_DB_CARDS = "@fluencytest:fluency_db_cards"
        const val FLUENCY_DB = "@fluencytest:fluency_db"
    }
}