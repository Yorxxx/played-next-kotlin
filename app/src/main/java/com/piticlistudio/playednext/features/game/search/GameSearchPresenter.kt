package com.piticlistudio.playednext.features.game.search

import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import com.piticlistudio.playednext.features.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * MvPPresenter implementation of [GameSearchContract.Presenter]
 */
class GameSearchPresenter constructor(private val searchCase: SearchGamesUseCase) : BasePresenter<GameSearchContract.View>(), GameSearchContract.Presenter {

    override fun search(query: String) {
        checkViewAttached()
        mvpView?.showLoading()
        mvpView?.hideError()
        searchCase.execute(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .doOnTerminate { mvpView?.hideLoading() }
                .subscribeBy(
                        onNext = { mvpView?.showData(it) },
                        onError = { mvpView?.showError(it) }
                )
    }
}