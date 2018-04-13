package com.piticlistudio.playednext.ui.game.search

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import com.piticlistudio.playednext.domain.interactor.game.SearchQuery
import com.piticlistudio.playednext.domain.model.Game
import javax.inject.Inject

/**
 * TiledDataSource for Game list results
 * Created by e-jegi on 09/11/2017.
 */
class SearchGamesPagedListProvider @Inject constructor(private val searchGamesUseCase: SearchGamesUseCase,
                                                       private val query: String? = null) : PageKeyedDataSource<Int, Game>() {

    val loadState = MutableLiveData<SearchStatus>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Game>) {
        query?.takeIf { !query.isNullOrEmpty() }?.let {
            try {
                loadState.postValue(SearchStatus.Loading())
                val response = searchGamesUseCase.execute(SearchQuery(it, 0, params.requestedLoadSize)).blockingLast()
                val nextPageKey = if (params.requestedLoadSize == response.size) response.size else null
                callback.onResult(response, 0, response.size, null, nextPageKey)
                loadState.postValue(SearchStatus.Success(response))
            } catch (e: Exception) {
                callback.onResult(listOf(), null, 1)
                loadState.postValue(SearchStatus.Failure(e))
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Game>) {
        query?.takeIf { !query.isNullOrEmpty() }?.let {
            try {
                loadState.postValue(SearchStatus.LoadingMore())
                val response = searchGamesUseCase.execute(SearchQuery(it, params.key, params.requestedLoadSize)).blockingLast()
                val adjacentKey = (if (params.requestedLoadSize == response.size) params.key + response.size else null)
                callback.onResult(response, adjacentKey)
                loadState.postValue(SearchStatus.Success(response))
            } catch (e: Exception) {
                callback.onResult(listOf(), params.key + 1)
                loadState.postValue(SearchStatus.Failure(e))
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Game>) {
        query?.takeIf { !query.isNullOrEmpty() }?.let {
            try {
                loadState.postValue(SearchStatus.LoadingMore())
                val response = searchGamesUseCase.execute(SearchQuery(it, params.key * params.requestedLoadSize, params.requestedLoadSize)).blockingLast()
                val adjacentKey = (if (params.key > 1) params.key - 1 else null)
                callback.onResult(response, adjacentKey)
                loadState.postValue(SearchStatus.Success(response))
            } catch (e: Exception) {
                callback.onResult(listOf(), params.key - 1)
                loadState.postValue(SearchStatus.Failure(e))
            }
        }
    }
}

sealed class SearchStatus {
    class Loading : SearchStatus()
    class LoadingMore : SearchStatus()
    class Failure(val error: Throwable) : SearchStatus()
    class Success(val items: List<Game>) : SearchStatus()
}