package com.piticlistudio.playednext.features.relation.load

import com.piticlistudio.playednext.domain.interactor.relation.LoadGameRelationUseCase
import com.piticlistudio.playednext.domain.interactor.relation.SaveGameRelationUseCase
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.features.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameRelationDetailPresenter @Inject constructor(private val loadUseCase: LoadGameRelationUseCase,
                                                      private val saveUseCase: SaveGameRelationUseCase) : BasePresenter<GameRelationDetailContract.View>(), GameRelationDetailContract.Presenter {

    override fun load(gameId: Int, platformId: Int) {
        checkViewAttached()
        loadUseCase.execute(Pair(gameId, platformId))
                .doOnSubscribe {
                    mvpView?.showLoading()
                    mvpView?.hideError()
                    mvpView?.hideData()
                }
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

    override fun save(data: GameRelation) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}