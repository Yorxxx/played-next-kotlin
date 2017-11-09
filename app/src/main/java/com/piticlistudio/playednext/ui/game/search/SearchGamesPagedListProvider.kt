package com.piticlistudio.playednext.ui.game.search

import android.arch.paging.LivePagedListProvider
import android.arch.paging.TiledDataSource
import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import com.piticlistudio.playednext.domain.model.Game
import javax.inject.Inject

/**
 * TiledDataSource for Game list results
 * Created by e-jegi on 09/11/2017.
 */
class SearchGamesPagedListProvider @Inject constructor(private val searchGamesUseCase: SearchGamesUseCase) : TiledDataSource<Game>() {

    private var results = mutableListOf<Game>()
    private var query: String? = null

    override fun countItems() = results.size

    override fun loadRange(startPosition: Int, count: Int): MutableList<Game> {
        query?.let {
            results.clear()
            results.addAll(searchGamesUseCase.execute(it).blockingFirst())
            return results
        }
        return mutableListOf()
    }

    fun setQueryFilter(searchQuery: String) {
        query = searchQuery
    }
}