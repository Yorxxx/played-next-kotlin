package com.piticlistudio.playednext.ui.game.search

import android.arch.paging.TiledDataSource
import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import com.piticlistudio.playednext.domain.interactor.game.SearchQuery
import com.piticlistudio.playednext.domain.model.Game
import javax.inject.Inject

/**
 * TiledDataSource for Game list results
 * Created by e-jegi on 09/11/2017.
 */
class SearchGamesPagedListProvider @Inject constructor(private val searchGamesUseCase: SearchGamesUseCase,
                                                       private val query: String? = null) : TiledDataSource<Game>() {

    private var results = mutableListOf<Game>()
    var listener: SearchGamesPagedListListener? = null

    override fun countItems() = results.size

    override fun loadRange(startPosition: Int, count: Int): MutableList<Game> {
        results.clear()
        query?.takeIf { !query.isNullOrEmpty() }?.let {
            try {
                results.addAll(searchGamesUseCase.execute(SearchQuery(it, startPosition, count)).blockingLast())
                listener?.onSearchCompleted(it, startPosition, count)
            } catch (e: Exception) {
                listener?.onSearchFailed(e)
            }
            return results
        }
        listener?.onSearchCompleted(null, startPosition, count)
        return results
    }
}

interface SearchGamesPagedListListener {
    fun onSearchCompleted(query: String?, startPosition: Int, count: Int)
    fun onSearchFailed(error: Exception)
}