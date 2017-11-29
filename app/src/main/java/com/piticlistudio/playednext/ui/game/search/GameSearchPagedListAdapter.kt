package com.piticlistudio.playednext.ui.game.search

import android.arch.paging.PagedListAdapter
import android.net.ConnectivityManager
import android.support.v7.recyclerview.extensions.DiffCallback
import android.view.ViewGroup
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.piticlistudio.playednext.domain.model.Game
import rx.subjects.BehaviorSubject
import javax.inject.Inject

class GameSearchPagedListAdapter @Inject constructor(private val connectivity: BehaviorSubject<Connectivity>) : PagedListAdapter<Game, GameSearchViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GameSearchViewHolder = GameSearchViewHolder(parent!!)

    override fun onBindViewHolder(holder: GameSearchViewHolder?, position: Int) {
        val game = getItem(position)
        var coverToLoad: String? = game?.cover?.smallUrl
        if (connectivity.value.type == ConnectivityManager.TYPE_WIFI) {
            coverToLoad = game?.cover?.bigUrl
        }
        holder?.bindTo(game, coverToLoad)
    }

    companion object {
        private val diffCallback = object : DiffCallback<Game>() {
            override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean = oldItem == newItem
        }
    }
}