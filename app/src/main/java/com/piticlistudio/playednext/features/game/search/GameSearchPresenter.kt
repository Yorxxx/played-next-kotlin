package com.piticlistudio.playednext.features.game.search

import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import com.piticlistudio.playednext.features.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * MvPPresenter implementation of [GameSearchContract.Presenter]
 */
class GameSearchPresenter @Inject constructor(private val searchCase: SearchGamesUseCase) : BasePresenter<GameSearchContract.View>(), GameSearchContract.Presenter {

    override fun search(query: String) {
        checkViewAttached()
        mvpView?.showLoading()
        mvpView?.hideError()
        searchCase.execute(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribeBy(
                        onNext = {
                            mvpView?.showData(it)
                            mvpView?.hideLoading()
                        },
                        onError = {
                            mvpView?.showError(it)
                            mvpView?.hideLoading()
                            mvpView?.hideData()
                        }
                )
    }
}