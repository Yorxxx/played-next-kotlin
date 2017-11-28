package com.piticlistudio.playednext.ui.gamerelation.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.relation.LoadRelationsForGameUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * ViewModel for GameRelationDetail view.
 */
class GameRelationDetailViewModel @Inject constructor(private val loadRelationsForGameUseCase: LoadRelationsForGameUseCase,
                                                      private val loadGameUseCase: LoadGameUseCase) : ViewModel() {

    private var disposable: Disposable? = null
    private val loadingStatus = MutableLiveData<Boolean>()
    private val relationStatus = MutableLiveData<List<GameRelation>>()
    private val errorStatus = MutableLiveData<Throwable?>()
    private val gameStatus = MutableLiveData<Game?>()
    private val imageStatus = MutableLiveData<String>()
    fun getLoading(): LiveData<Boolean> = loadingStatus
    fun getRelations(): LiveData<List<GameRelation>> = relationStatus
    fun getError(): LiveData<Throwable?> = errorStatus
    fun getGame(): LiveData<Game?> = gameStatus
    fun getScreenshot(): LiveData<String> = imageStatus

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun loadRelationForGame(gameId: Int) {
        loadGameUseCase.execute(gameId)
                .doOnNext {
                    gameStatus.postValue(it)
                    loadImageToShow(it)
                }
                .flatMap { loadRelationsForGameUseCase.execute(it) }
                .doOnSubscribe { loadingStatus.postValue(true) }
                .subscribeOn(Schedulers.io())
                .toObservable()
                .subscribeBy(
                        onNext = {
                            relationStatus.postValue(it)
                            loadingStatus.postValue(false)
                            errorStatus.postValue(null)
                        },
                        onError = {
                            gameStatus.postValue(null)
                            relationStatus.postValue(null)
                            errorStatus.postValue(it)
                            loadingStatus.postValue(false)
                        }
                )
    }

    private fun loadImageToShow(data: Game) {
        data.images?.let {
            if (it.isNotEmpty()) {
                imageStatus.postValue(it[0].mediumSizeUrl)
                disposable = Flowable.interval(5, TimeUnit.SECONDS)
                    .take( it.size.toLong())
                    .map { data.images!!.get(it.toInt()) }
                    .repeat()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { imageStatus.postValue(it.mediumSizeUrl) }
            }
        }
    }

    private fun loadImageToShow(data: Game) {
        data.images?.let {
            if (it.isNotEmpty()) {
                imageStatus.postValue(it[0].mediumSizeUrl)
                disposable = Flowable.interval(5, TimeUnit.SECONDS)
                    .take( it.size.toLong())
                    .map { data.images!!.get(it.toInt()) }
                    .repeat()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { imageStatus.postValue(it.mediumSizeUrl) }
            }
        }
    }
}