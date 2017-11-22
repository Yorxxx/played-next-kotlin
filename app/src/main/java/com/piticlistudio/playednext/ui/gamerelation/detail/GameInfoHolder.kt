package com.piticlistudio.playednext.ui.gamerelation.detail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.domain.model.Game
import kotlinx.android.synthetic.main.game_detail_info_item_row.view.*

class GameInfoHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.game_detail_info_item_row, parent, false)
) {

    fun bindTo(item: Game?) {
        item?.let {
            itemView.developer.text = it.developers?.joinToString(transform = { it.name })
            itemView.genre.text = it.genres?.joinToString(transform = { it.name })
            itemView.publisher.text = it.publishers?.joinToString(transform = { it.name })
            it.collection?.let {
                itemView.saga.text = it.name
            }
        }
    }

}