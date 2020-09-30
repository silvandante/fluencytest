package com.walker.fluencytest.ui.home

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.walker.fluencytest.FluenyTestApplication
import com.walker.fluencytest.data.constants.AppConstants
import com.walker.fluencytest.data.local.SecurityPreferences
import com.walker.fluencytest.data.models.cards.dto.CardDataDto
import com.walker.fluencytest.data.models.cards.dto.CardDto
import com.walker.fluencytest.data.models.cards.dto.NoteDto
import com.walker.fluencytest.data.models.cards.dto.ScoreDto
import com.walker.fluencytest.data.models.decks.dto.DeckDataDto
import com.walker.fluencytest.data.models.decks.dto.RankDto
import com.walker.fluencytest.data.models.decks.dto.TagDto
import com.walker.fluencytest.data.models.decks.serialized.DecksModel
import com.walker.fluencytest.data.network.listener.APIListener
import com.walker.fluencytest.data.network.repositories.DeckRepository
import com.walker.fluencytest.ui.card.CardsModelView
import io.realm.RealmList
import kotlinx.android.synthetic.main.activity_feed_deck.*
import org.jsoup.Jsoup


class FeedDeckActivity : AppCompatActivity() {


    private val decksRepository = DeckRepository()

    private lateinit var feedDeckViewModel: FeedDeckViewModel
    private lateinit var cardsModelView: CardsModelView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.walker.fluencytest.R.layout.activity_feed_deck)
        val myApp = this.applicationContext as FluenyTestApplication

        val userId = getIntent().getStringExtra(AppConstants.SHARED_PREFERENCES.USER_ID).toString()

        val downloadmanager: DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        feedDeckViewModel = ViewModelProvider(this).get(FeedDeckViewModel::class.java)
        cardsModelView = ViewModelProvider(this).get(CardsModelView::class.java)

        if(!feedDeckViewModel.hasDecks)
            if(myApp.getIsConnected()) {
                decksRepository.getDecks(userId, object : APIListener<DecksModel> {
                        override fun onSuccess(model: DecksModel) {

                            val deck = model.data.filter { it.deckId == 642 }[0]
                            var newDeck = DeckDataDto()

                            newDeck.authorId = deck.authorId
                            newDeck.cardCount = deck.cardCount
                            newDeck.cardsPerSession = deck.cardsPerSession
                            newDeck.deckId = deck.deckId
                            newDeck.isPublic = deck.isPublic
                            newDeck.kind = deck.kind
                            newDeck.lastLearned = deck.lastLearned
                            newDeck.nonOverdue = deck.nonOverdue

                            var newRank = RankDto()

                            newRank.score = deck.rank.score

                            var newTags: RealmList<TagDto> = RealmList()

                            for (tag in deck.tags) {
                                var newTag = TagDto()

                                newTag.deckId = tag.deckId
                                newTag.tag = tag.tag
                                newTag.id = tag.id

                                newTags.add(newTag)
                            }


                            newDeck.rank = newRank
                            newDeck.tags = newTags
                            newDeck.title = deck.title
                            newDeck.virtualId = deck.virtualId

                            Log.d("inseriu", newDeck.deckId.toString())

                            feedDeckViewModel.insertDeck(newDeck)

                            cardsModelView.getCardsFromNetwork(newDeck.deckId.toString())

                        }

                        override fun onError(str: String) {

                        }

                        override fun onFailure(model: DecksModel) {

                        }

                    })
            } else {
                text_error_online.visibility = View.VISIBLE
            }
        else
            progress_bar_deck_loading.visibility = View.GONE

        val mSharedPreferences = SecurityPreferences(this)

        user_deck_name.text = "Oi, "+mSharedPreferences.get(AppConstants.SHARED_PREFERENCES.USER_NAME)

        val deckListAdapter = FeedDeckListAdapter(this)

        val gridLayoutManager = GridLayoutManager(this, 2)

        rv_deck_list.layoutManager = gridLayoutManager
        rv_deck_list.setHasFixedSize(true)
        rv_deck_list.adapter = deckListAdapter

        cardsModelView.cards.observe(this, Observer {
            if (it != null) {
                var count = 0;

                for (card in it) {

                    var newCard = CardDataDto()

                    var newCardCard = CardDto()
                    newCardCard.cardType = card.card.cardType
                    newCardCard.dateCreated = card.card.dateCreated
                    newCardCard.dateUpdated = card.card.dateUpdated
                    newCardCard.deckId = card.card.deckId

                    var newCardScore = ScoreDto()
                    newCardScore.cardId = card.score.cardId
                    newCardScore.cca = card.score.cca
                    newCardScore.dateCreated = card.score.dateCreated
                    newCardScore.due = card.score.due
                    newCardScore.dateUpdated = card.score.dateUpdated
                    newCardScore.easiness = card.score.easiness
                    newCardScore.reviews = card.score.reviews

                    newCard.card = newCardCard
                    newCard.score = newCardScore

                    var newNotes: RealmList<NoteDto> = RealmList()

                    val fieldParsed = Jsoup.parse(card.notes[0]?.field).getElementsByTag("source")

                    val audio: String = fieldParsed.attr("src")

                    val uri = Uri.parse(audio.replace("https://","http://"))

                    newCard.originalUrlAudio = uri.toString()

                    val request = DownloadManager.Request(uri)

                    request.setDestinationInExternalFilesDir(
                        this,
                        Environment.DIRECTORY_DOWNLOADS,
                        "/cards_"+card.card.id+"_audio.mp3"
                    )

                    newCard.pathAudio = "/cards_"+card.card.id+"_audio.mp3"

                    downloadmanager.enqueue(request)

                    for (note in card.notes) {
                        var newNote = NoteDto()
                        newNote.cardId = note.cardId
                        newNote.dateCreated = note.dateCreated
                        newNote.dateUpdated = note.dateUpdated
                        newNote.field = note.field
                        newNote.fieldName = note.fieldName
                        newNote.fieldNumber = note.fieldNumber
                        newNote.id = note.id
                        newNote.noteType = note.noteType
                        newNotes.add(newNote)
                    }


                    newCard.notes = newNotes
                    count++
                    cardsModelView.insertCard(newCard)
                }
            } else {
                Toast.makeText(
                    this,
                    "Não foi possível recuperar cards desse deck.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            progress_bar_deck_loading.visibility = View.GONE
        })

        feedDeckViewModel.decks.observe(this, Observer {
            if (it != null) {
                if (deckListAdapter.itemCount <= 1) {
                    deckListAdapter.submitList(it)
                } else {
                    Toast.makeText(this, "Sincronização concluída.", Toast.LENGTH_SHORT).show()
                    deckListAdapter.notifyChanges(deckListAdapter.currentList, it)
                }
            } else {
                progress_bar_deck_loading.visibility = View.GONE
                Toast.makeText(
                    this,
                    "Recuperação de decks falhou! Tente novamente.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


}