package com.piticlistudio.playednext.ui.game.search

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.piticlistudio.playednext.domain.model.Game
import javax.inject.Inject

class GameSearchPagedListAdapter @Inject constructor() : PagedListAdapter<Game, GameSearchViewHolder>(diffCallback) {

    var onClickListener: ((Game) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameSearchViewHolder = GameSearchViewHolder(parent)

    override fun onBindViewHolder(holder: GameSearchViewHolder, position: Int) {
        val game = getItem(position)
        onClickListener?.let {
            holder.bindTo(game, game?.cover?.url, it)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Game>() {

            override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean = oldItem == newItem
        }
    }
}