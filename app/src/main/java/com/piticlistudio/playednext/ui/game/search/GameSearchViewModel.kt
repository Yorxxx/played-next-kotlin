package com.piticlistudio.playednext.ui.game.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListProvider
import android.arch.paging.PagedList
import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import com.piticlistudio.playednext.domain.model.Game
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class GameSearchViewModel @Inject constructor(private val searchGamesUseCase: SearchGamesUseCase) : ViewModel(), SearchGamesPagedListListener {

    private val TAG = "GameSearchViewModel"

    private val loadingLiveData = MutableLiveData<Boolean>()
    fun getLoading(): LiveData<Boolean> = loadingLiveData

    private var provider: SearchGamesPagedListProvider? = null

    private var searchResultsLiveData: LiveData<PagedList<Game>>? = null
    val searchResults: LiveData<PagedList<Game>>
        get() {
            if (searchResultsLiveData == null) {
                searchResultsLiveData = getData(null)
            }
            return searchResultsLiveData ?: throw AssertionError("Check your threads")
        }
    private var disposable: Disposable? = null

    init {
        //provider.listener = this
        loadingLiveData.postValue(false)
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    fun setQueryFilter(query: String) {
        loadingLiveData.postValue(true)
        searchResultsLiveData = getData(query)
    }

    private fun getData(query: String?): LiveData<PagedList<Game>> {

        val p = object : LivePagedListProvider<Int, Game>() {
            override fun createDataSource(): DataSource<Int, Game> {
                return SearchGamesPagedListProvider(searchGamesUseCase, query).also { it.listener = this@GameSearchViewModel }
            }
        }

        return p.create(0, PagedList.Config.Builder()
                .setPageSize(10) //number of items loaded at once
                .setInitialLoadSizeHint(10)
                .setEnablePlaceholders(false)
                .build())
    }

    override fun onSearchCompleted(query: String?, startPosition: Int, count: Int) = loadingLiveData.postValue(false)
}