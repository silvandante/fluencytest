package com.walker.fluencytest.ui.card

import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.walker.fluencytest.R
import com.walker.fluencytest.data.constants.AppConstants
import com.walker.fluencytest.data.models.cards.dto.CardDataDto
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt


class CardActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var cardsViewModel: CardsModelView
    //private lateinit var cardModelList: ArrayList<CardModel>
    private lateinit var cardAdapter: CardAdapter
    var scale: Float = 0.0f



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val downloadmanager: DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val deck_id = getIntent().getStringExtra(AppConstants.SHARED_PREFERENCES.DECK_TAG).toString()
        val deck_title = getIntent().getStringExtra(AppConstants.SHARED_PREFERENCES.DECK_TITLE).toString()
        val deck_tags = getIntent().getStringExtra(AppConstants.SHARED_PREFERENCES.DECK_TAGS).toString()

        cardsViewModel = ViewModelProvider(this).get(CardsModelView::class.java)

        deck_title_card.text = deck_title
        deck_tags_card.text = deck_tags

        setListeners()

        handleLoadCards(deck_id,downloadmanager)
    }


    override fun onClick(v: View) {
        if (v.id == R.id.buttonGetBack) {
            getBackToFeed()
        }
    }


    private fun setListeners() {
        buttonGetBack.setOnClickListener(this)
    }

    /**
     * Pega cards de um deck pelo id
     */
    private fun handleLoadCards(deck_id: String, downloadmanager: DownloadManager) {

        var cards = cardsViewModel.getCards(deck_id)!!

        loadCards(cards,downloadmanager)

    }

    private fun getBackToFeed(){
        this.finish()
    }



    private fun loadCards(cards: List<CardDataDto>, downloadmanager: DownloadManager) {



        scale = applicationContext.resources.displayMetrics.density

        progressBar.max = cards.size
        progressBar.progress = 1
        percentageText.text = ((1.toDouble() / cards.size) * 100).roundToInt().toString()+"%"
        manyToGoText.text = "1/"+cards.size.toString()



        cardAdapter = CardAdapter(
            this,
            cards,
            scale,
            viewPager,
            progressBar,
            percentageText,
            manyToGoText,
            downloadmanager
        )

        viewPager.adapter = cardAdapter

    }
}

