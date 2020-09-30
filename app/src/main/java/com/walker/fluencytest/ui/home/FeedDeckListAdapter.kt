package com.walker.fluencytest.ui.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.walker.fluencytest.R
import com.walker.fluencytest.data.constants.AppConstants
import com.walker.fluencytest.data.models.decks.dto.DeckDataDto
import com.walker.fluencytest.data.models.decks.serialized.Data
import com.walker.fluencytest.ui.card.CardActivity
import kotlinx.android.synthetic.main.card_item.view.*
import kotlinx.android.synthetic.main.deck_item.view.*

class FeedDeckListAdapter(val context: Context) : ListAdapter<DeckDataDto, RecyclerView.ViewHolder>(DeckDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View

        view = layoutInflater.inflate(R.layout.deck_item, parent, false)
        return DeckItemViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DeckItemViewHolder).bind(getItem(position),context)
    }

    class DeckDiffCallback : DiffUtil.ItemCallback<DeckDataDto>() {
        override fun areItemsTheSame(oldItem: DeckDataDto, newItem: DeckDataDto): Boolean {
            return oldItem.deckId == newItem.deckId
        }

        override fun areContentsTheSame(oldItem: DeckDataDto, newItem: DeckDataDto): Boolean {
            return oldItem == newItem
        }

    }

    fun notifyChanges(oldList: List<DeckDataDto>, newList: List<DeckDataDto>) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].deckId == newList[newItemPosition].deckId
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size
        })
        diff.dispatchUpdatesTo(this)
    }

    class DeckItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(deck: DeckDataDto?, context: Context){
            itemView.deck_title.text = deck?.title

            Log.d("new_adaptor", deck?.title.toString())

            var tags: String = ""

            var deckImage: Int

            if (deck?.tags?.isEmpty() == false) {
                for (tag in deck?.tags!!.indices)
                    if (tag + 1 == deck?.tags.size)
                        tags = deck?.tags[tag]?.tag.toString()
                    else
                        tags = deck?.tags[tag]?.tag.toString() + " | " + tag


                deckImage = when(deck?.tags[0]?.tag.toString()){
                    "english" -> R.drawable.usa
                    "french" -> R.drawable.france
                    "spanish" -> R.drawable.spanish
                    else -> R.drawable.others
                }

                itemView.deck_tags.text = tags

            } else {
                deckImage = R.drawable.others
            }

            itemView.visualizer

            itemView.deck_image.setImageResource(deckImage)

            itemView.setOnClickListener{
                var intent = Intent(context, CardActivity::class.java)
                intent.putExtra(AppConstants.SHARED_PREFERENCES.DECK_TAG, deck?.deckId.toString())
                intent.putExtra(AppConstants.SHARED_PREFERENCES.DECK_TITLE, deck?.title.toString())
                intent.putExtra(AppConstants.SHARED_PREFERENCES.DECK_TAGS, tags)
                context.startActivity(intent)
            }

        }
    }

}