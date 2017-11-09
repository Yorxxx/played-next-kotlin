package com.piticlistudio.playednext.ui.game.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.domain.model.Game
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.gamesearch_item.view.*

class GameSearchViewHolder (parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.gamesearch_item, parent, false)) {

    var item: Game? = null

    fun bindTo(item: Game?) {
        this.item = item
        itemView.title.text = item?.name?.capitalize()
        item?.cover?.apply {
            Picasso.with(itemView.context)
                    .load(url)
                    .into(itemView.image)
        }
    }
}