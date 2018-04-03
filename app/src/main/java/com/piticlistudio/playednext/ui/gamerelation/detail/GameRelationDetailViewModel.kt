package com.piticlistudio.playednext.ui.gamerelation.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.relation.LoadRelationsForGameUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.ui.PlatformUIUtils
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
    private val viewState = MutableLiveData<ViewState>()
    fun getCurrentState(): LiveData<ViewState> = viewState

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    private fun currentViewState(): ViewState = viewState.value!!

    fun loadRelationForGame(gameId: Int) {
        loadGameUseCase.execute(gameId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewState.value = currentViewState().copy(game = it)
                    loadImageToShow(it)
                }
                .observeOn(Schedulers.io())
                .flatMap { loadRelationsForGameUseCase.execute(it) }
                .doOnSubscribe { viewState.postValue(ViewState(true, listOf(), null, null, null)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribeBy(
                        onNext = {
                            viewState.value = currentViewState().copy(relations = it, isLoading = false, error = null)
                        },
                        onError = {
                            viewState.value = currentViewState().copy(isLoading = false, error = it)
                        }
                )
    }

    private fun loadImageToShow(data: Game) {
        data.images?.let {
            if (it.isNotEmpty()) {
                viewState.value = currentViewState().copy(showImage = it[0].mediumSizeUrl)
                disposable = Flowable.interval(10, TimeUnit.SECONDS)
                        .take(it.size.toLong())
                        .map { data.images!!.get(it.toInt()) }
                        .repeat()
                        .observeOn(AndroidSchedulers.mainThread())
                        .toObservable()
                        .subscribeBy(
                                onNext = {
                                    viewState.value = currentViewState().copy(showImage = it.mediumSizeUrl)
                                },
                                onError = {
                                    viewState.value = currentViewState().copy()
                                }
                        )
            }
        }
    }
}

data class ViewState(val isLoading: Boolean = false, val relations: List<GameRelation>,
                     val error: Throwable? = null, val game: Game? = null, val showImage: String? = null)