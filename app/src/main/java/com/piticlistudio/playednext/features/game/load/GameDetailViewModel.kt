package com.piticlistudio.playednext.features.game.load

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.model.Game
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameDetailViewModel @Inject constructor(private val loadGameUseCase: LoadGameUseCase): ViewModel() {

    private val loadingStatus = MutableLiveData<Boolean>()
    private val gameStatus = MutableLiveData<Game>()
    fun getLoadingStatus(): LiveData<Boolean> = loadingStatus
    fun getGameStatus(): LiveData<Game> = gameStatus

    fun load(gameId: Int) {
        loadGameUseCase.execute(gameId)
                .doOnSubscribe { loadingStatus.postValue(true)}
                .subscribeOn(Schedulers.io())
                .toObservable()
                .subscribeBy(
                        onNext = {
                            gameStatus.postValue(it)
                            loadingStatus.postValue(false)
                        },
                        onError = {
                            loadingStatus.postValue(false)
                        }
                )
    }
}