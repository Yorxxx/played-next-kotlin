package com.piticlistudio.playednext.ui.game.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.net.NetworkInfo
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.util.NoNetworkAvailableException
import rx.Subscription
import rx.subjects.BehaviorSubject
import java.util.concurrent.Executors
import javax.inject.Inject


class GameSearchViewModel @Inject constructor(private val searchGamesUseCase: SearchGamesUseCase,
                                              connectivity: BehaviorSubject<Connectivity>) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()
    private var factoryLiveData = MutableLiveData<GameSearchFactory>()
    private var searchResultsLiveData: LiveData<PagedList<Game>>? = null
    private val viewState = MutableLiveData<GameSearchViewState>()
    private fun currentViewState(): GameSearchViewState {
        if (viewState.value != null) {
            return viewState.value!!
        }
        return GameSearchViewState()
    }

    private var disposable: Subscription? = null

    init {
        disposable = connectivity.subscribe {
            when (it.state) {
                NetworkInfo.State.CONNECTED -> {
                    if (currentViewState().error is NoNetworkAvailableException) {
                        viewState.postValue(currentViewState().copy(error = null, searchEnabled = true))
                    }
                }
                else -> {
                    viewState.postValue(currentViewState().copy(error = NoNetworkAvailableException(), searchEnabled = false))
                }
            }
        }
    }

    fun setQueryFilter(query: String) {
        viewState.postValue(currentViewState().copy(searchInput = query))
        queryLiveData.postValue(query)
    }

    fun getData(): LiveData<PagedList<Game>> {

        return switchMap(queryLiveData, {
            val factory = GameSearchFactory(searchGamesUseCase, it)
            factoryLiveData.postValue(factory)

            val config = PagedList.Config.Builder()
                    .setInitialLoadSizeHint(24)
                    .setPageSize(24)
                    .build()

            searchResultsLiveData = LivePagedListBuilder<Int, Game>(factory, config)
                    .setInitialLoadKey(1)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .build()
            searchResultsLiveData!!
        })
    }

    private fun getSearchStatus(): LiveData<SearchStatus> {
        return switchMap(switchMap(factoryLiveData, { factory ->
            factory.datasourceLiveData
        }), { provider ->
            provider.loadState
        })
    }

    fun getCurrentState(): LiveData<GameSearchViewState> {
        return switchMap(getSearchStatus(), {
            when (it) {
                is SearchStatus.Loading -> viewState.postValue(currentViewState().copy(isLoading = true, error = null, searchEnabled = true, showSearchIsEmpty = false))
                is SearchStatus.LoadingMore -> viewState.postValue(currentViewState().copy(isLoading = true, error = null, searchEnabled = true, showSearchIsEmpty = false))
                is SearchStatus.Failure -> viewState.postValue(currentViewState().copy(isLoading = false, error = it.error, searchEnabled = true, showSearchIsEmpty = false))
                is SearchStatus.Success -> viewState.postValue(currentViewState().copy(isLoading = false, error = null, searchEnabled = true, showSearchIsEmpty = it.items.isEmpty()))
            }
            viewState
        })
    }
}

data class GameSearchViewState(val isLoading: Boolean = false,
                               val showSearchIsEmpty: Boolean = false,
                               val error: Throwable? = null,
                               val searchEnabled: Boolean? = true,
                               val searchInput: String? = null)