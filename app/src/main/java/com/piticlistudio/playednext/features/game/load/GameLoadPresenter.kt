package com.piticlistudio.playednext.features.game.load

import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.game.SaveGameUseCase
import com.piticlistudio.playednext.features.base.BasePresenter
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameLoadPresenter @Inject constructor(private val loadGameUseCase: LoadGameUseCase,
                                            private val saveGameUseCase: SaveGameUseCase) : BasePresenter<GameLoadContract.View>(), GameLoadContract.Presenter {

    override fun load(id: Int) {
        checkViewAttached()
        mvpView?.showLoading()
        loadGameUseCase.execute(id)
                .flatMap { saveGameUseCase.execute(it).andThen(Flowable.just(it)).onErrorReturnItem(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .doOnTerminate { mvpView?.hideLoading() }
                .subscribeBy(
                        onNext = {
                            mvpView?.showData(it)
                            mvpView?.hideError()
                        },
                        onError = {
                            mvpView?.showError(it)
                            mvpView?.hideData()
                        }
                )
    }
}