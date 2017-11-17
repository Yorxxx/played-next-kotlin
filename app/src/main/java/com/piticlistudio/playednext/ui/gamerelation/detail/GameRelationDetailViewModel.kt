package com.piticlistudio.playednext.ui.gamerelation.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.game.SaveGameUseCase
import com.piticlistudio.playednext.domain.interactor.relation.LoadRelationsForGameUseCase
import com.piticlistudio.playednext.domain.interactor.relation.SaveGameRelationUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.gamerelation_detail.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * ViewModel for GameRelationDetail view.
 */
class GameRelationDetailViewModel @Inject constructor(private val loadRelationsForGameUseCase: LoadRelationsForGameUseCase,
                                                      private val loadGameUseCase: LoadGameUseCase,
                                                      private val saveGameUseCase: SaveGameUseCase,
                                                      private val saveGameRelationUseCase: SaveGameRelationUseCase) : ViewModel() {

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

    fun saveRelation(relation: GameRelation) {
        if (relation.game == null) throw RuntimeException("Relation does not have game")
        if (relation.platform == null) throw RuntimeException("Relation does not have game")

        saveGameUseCase.execute(relation.game!!)
                .andThen(saveGameRelationUseCase.execute(relation))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { errorStatus.postValue(it) },
                        onComplete = { errorStatus.postValue(null)}
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
}