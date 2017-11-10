package com.piticlistudio.playednext.ui.game.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.domain.model.Game
import kotlinx.android.synthetic.main.gamesearch_item.view.*

class GameSearchViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.gamesearch_item, parent, false)) {

    var item: Game? = null

    fun bindTo(item: Game?) {
        this.item = item
        itemView.title.text = item?.name?.capitalize()

        item?.let {
            if (item.cover != null) {
                Glide.with(itemView.context)
                        .load(item.cover.url)
                        .apply(RequestOptions().placeholder(R.drawable.ic_image_black_24dp))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(itemView.image)
            } else {
                Glide.with(itemView.context).clear(itemView.image)
                itemView.image.setImageResource(R.drawable.ic_image_black_24dp)
            }
        }
    }
}