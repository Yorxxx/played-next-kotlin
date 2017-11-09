package com.piticlistudio.playednext.ui.game.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListProvider
import android.arch.paging.PagedList
import com.piticlistudio.playednext.domain.model.Game
import javax.inject.Inject

class GameSearchViewModel @Inject constructor(private val provider: SearchGamesPagedListProvider) : ViewModel() {

    private var searchResultsLiveData: LiveData<PagedList<Game>>? = null
    val searchResults: LiveData<PagedList<Game>>
        get() {
            if (searchResultsLiveData == null) {
                searchResultsLiveData = getData()
            }
            return searchResultsLiveData ?: throw AssertionError("Check your threads")
        }

    fun setQueryFilter(query: String) {
        provider.setQueryFilter(query)
        searchResultsLiveData = null
    }

    private fun getData(): LiveData<PagedList<Game>> {

        val p = object : LivePagedListProvider<Int, Game>() {
            override fun createDataSource(): DataSource<Int, Game> {
                return provider
            }
        }

        return p.create(0, PagedList.Config.Builder()
                .setPageSize(5) //number of items loaded at once
                .setInitialLoadSizeHint(5)
                .setEnablePlaceholders(false)
                .build())
    }
}