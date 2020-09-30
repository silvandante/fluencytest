package com.walker.fluencytest

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Color.*
import android.net.*
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.util.Util
import com.google.android.material.snackbar.Snackbar
import com.walker.fluencytest.data.constants.AppConstants
import com.walker.fluencytest.data.local.DecksRealmManager
import com.walker.fluencytest.data.local.SecurityPreferences
import com.walker.fluencytest.data.models.decks.dto.DeckDataDto
import com.walker.fluencytest.data.models.decks.dto.RankDto
import com.walker.fluencytest.data.models.decks.dto.TagDto
import com.walker.fluencytest.data.models.decks.serialized.Data
import com.walker.fluencytest.data.models.decks.serialized.DecksModel
import com.walker.fluencytest.data.network.listener.APIListener
import com.walker.fluencytest.data.network.repositories.DeckRepository
import kotlinx.android.synthetic.main.activity_feed_deck.*
import io.realm.Realm
import io.realm.RealmList
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class FluenyTestApplication : Application() {

    companion object {
        var context: Context? = null
    }

    private var isConnected: Boolean = false

    fun getIsConnected() = isConnected
    fun setIsConnected (connectivity: Boolean) {
        isConnected = connectivity
    }


    private val mDecks = MutableLiveData<List<Data>>()
    val decks: LiveData<List<Data>> = mDecks


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Realm.init(this)
        Log.d("chamou app", "app chamado")

        val cm:ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        val mSharedPreferences = SecurityPreferences(this)

        val decksRepository = DeckRepository()

        cm.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    Log.i("CONNECTIVITY_STATUS", "AVAILABLE!")
                    Toast.makeText(context, "APP online. Sincronizando...", Toast.LENGTH_SHORT).show()
                    setIsConnected(true)
                    val userId = mSharedPreferences.get(AppConstants.SHARED_PREFERENCES.USER_ID)

                    var decksRealmManager: DecksRealmManager = DecksRealmManager()

                    if (!userId.isNullOrEmpty())
                        decksRepository.getDecks(userId, object : APIListener<DecksModel> {
                            override fun onSuccess(model: DecksModel) {
                                    val filtered = model.data.filter{ it.deckId == 642 }[0]

                                        var newDeck = DeckDataDto()
                                        newDeck.authorId = filtered.authorId
                                        newDeck.cardCount = filtered.cardCount
                                        newDeck.cardsPerSession = filtered.cardsPerSession
                                        newDeck.deckId = filtered.deckId
                                        newDeck.isPublic = filtered.isPublic
                                        newDeck.kind = filtered.kind
                                        newDeck.lastLearned = filtered.lastLearned
                                        newDeck.nonOverdue = filtered.nonOverdue

                                        var newRank = RankDto()

                                        newRank.score = filtered.rank.score

                                        var newTags: RealmList<TagDto> = RealmList()

                                        for (tag in filtered.tags) {
                                            var newTag = TagDto()

                                            newTag.deckId = tag.deckId
                                            newTag.tag = tag.tag
                                            newTag.id = tag.id

                                            newTags.add(newTag)
                                        }


                                        newDeck.rank = newRank
                                        newDeck.tags = newTags
                                        newDeck.title = filtered.title
                                        newDeck.virtualId = filtered.virtualId
                                        Log.d("inseriu_app", newDeck.deckId.toString())

                                        decksRealmManager.insertDeck(newDeck)

                            }

                            override fun onError(str: String) {

                            }

                            override fun onFailure(model: DecksModel) {
                            }

                        })
                }

                override fun onLost(network: Network) {
                    setIsConnected(false)
                    Toast.makeText(context, "APP modo offline", Toast.LENGTH_SHORT).show()
                    Log.i("CONNECTIVITY_STATUS", "LOST!")
                }
            }
        )

    }



}
