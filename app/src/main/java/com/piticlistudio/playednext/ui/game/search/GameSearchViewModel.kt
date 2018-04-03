package com.piticlistudio.playednext.ui.game.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListProvider
import android.arch.paging.PagedList
import android.arch.paging.TiledDataSource
import android.net.NetworkInfo
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.util.NoNetworkAvailableException
import rx.Subscription
import rx.subjects.BehaviorSubject
import java.net.UnknownHostException
import javax.inject.Inject

class GameSearchViewModel @Inject constructor(private val searchGamesUseCase: SearchGamesUseCase,
                                              connectivity: BehaviorSubject<Connectivity>) : ViewModel(), SearchGamesPagedListListener {

    private val loadingLiveData = MutableLiveData<Boolean>()
    fun getLoading(): LiveData<Boolean> = loadingLiveData

    private val errorLiveData = MutableLiveData<Exception>()
    fun getError(): LiveData<Exception> = errorLiveData

    private var searchResultsLiveData: LiveData<PagedList<Game>>? = null
    val searchResults: LiveData<PagedList<Game>>
        get() {
            if (searchResultsLiveData == null) {
                searchResultsLiveData = getData(null)
            }
            return searchResultsLiveData ?: throw AssertionError("Check your threads")
        }
    private var disposable: Subscription? = null
    var provider: TiledDataSource<Game>? = null
    private var startPosition = 0
    private var count = 0
    private var shouldRestoreConnection = false
    private var query: String? = null

    init {
        disposable = connectivity.subscribe {
            when (it.state) {
                NetworkInfo.State.CONNECTED -> {
                    errorLiveData.postValue(null)
                    provider?.let {
                        if (shouldRestoreConnection && query != null) {
                            setQueryFilter(query!!)
                        }
                    }
                }
                else -> {
                    errorLiveData.postValue(NoNetworkAvailableException())
                    if (this.startPosition > 0 && this.count > 0) {
                        shouldRestoreConnection = true
                    }
                }
            }
        }
        loadingLiveData.postValue(false)
    }

    override fun onCleared() {
        disposable?.unsubscribe()
        super.onCleared()
    }

    fun setQueryFilter(query: String) {
        this.query = query
        loadingLiveData.postValue(true)
        provider?.invalidate()
        searchResultsLiveData = getData(query)
    }

    private fun getData(query: String?): LiveData<PagedList<Game>> {

        val p = object : LivePagedListProvider<Int, Game>() {
            override fun createDataSource(): DataSource<Int, Game> {
                return SearchGamesPagedListProvider(searchGamesUseCase, query).also {
                    it.listener = this@GameSearchViewModel
                    provider = it
                }
            }
        }

        return p.create(0, PagedList.Config.Builder()
                .setPageSize(12) //number of items loaded at once
                .setEnablePlaceholders(false)
                .build())
    }

    override fun onSearchCompleted(query: String?, startPosition: Int, count: Int) {
        loadingLiveData.postValue(false)
        this.startPosition = startPosition
        this.count = count
    }

    override fun onSearchFailed(error: Exception) {
        when (error) {
            is UnknownHostException -> errorLiveData.postValue(NoNetworkAvailableException())
            is RuntimeException -> {
                when (error.cause) {
                    is UnknownHostException -> errorLiveData.postValue(NoNetworkAvailableException())
                    else -> errorLiveData.postValue(error)
                }
            }
            else -> errorLiveData.postValue(error)
        }
    }
}