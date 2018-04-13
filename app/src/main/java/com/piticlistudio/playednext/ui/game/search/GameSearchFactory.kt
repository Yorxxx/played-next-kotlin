package com.piticlistudio.playednext.ui.game.search

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import com.piticlistudio.playednext.domain.model.Game
import javax.inject.Inject

class GameSearchFactory @Inject constructor(private val searchGamesUseCase: SearchGamesUseCase,
                                            private val query: String? = null) : DataSource.Factory<Int, Game>() {

    val datasourceLiveData = MutableLiveData<SearchGamesPagedListProvider>()

    override fun create(): DataSource<Int, Game> {
        val source = SearchGamesPagedListProvider(searchGamesUseCase, query)
        datasourceLiveData.postValue(source)
        return source
    }
}