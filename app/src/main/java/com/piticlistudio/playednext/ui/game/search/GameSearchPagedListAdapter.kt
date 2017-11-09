package com.piticlistudio.playednext.ui.game.search

import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.view.ViewGroup
import com.piticlistudio.playednext.domain.model.Game

class GameSearchPagedListAdapter : PagedListAdapter<Game, GameSearchViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GameSearchViewHolder = GameSearchViewHolder(parent!!)

    override fun onBindViewHolder(holder: GameSearchViewHolder?, position: Int) {
        holder?.bindTo(getItem(position))
    }

    companion object {
        private val diffCallback = object : DiffCallback<Game>() {
            override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean = oldItem == newItem
        }
    }
}