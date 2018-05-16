package com.piticlistudio.playednext.ui.playlists.overview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.piticlistudio.playednext.domain.interactor.playlists.LoadAllPlaylistsUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.ui.playlists.overview.mapper.PlaylistsOverviewModelMapper
import com.piticlistudio.playednext.ui.playlists.overview.model.PlaylistsOverviewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * View model for displaying an overview of the playlists
 */
class PlaylistsOverviewViewModel @Inject constructor(private val useCase: LoadAllPlaylistsUseCase,
                                                     private val mapper: PlaylistsOverviewModelMapper) : ViewModel() {

    private var disposable: Disposable? = null
    private val viewState = MutableLiveData<ViewState>()
    fun getViewState(): LiveData<ViewState> = viewState

    private fun currentViewState(): ViewState = viewState.value!!

    fun start() {
        disposable = useCase.execute()
                .flatMapSingle {
                    Flowable.fromIterable(it)
                            .map { mapper.mapIntoPresentationModel(it) }
                            .toList()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.postValue(ViewState(isLoading = true, error = null, items = emptyList()))
                }
                .doOnEach {
                    viewState.postValue(currentViewState().copy(isLoading = false))
                }
                .subscribeOn(Schedulers.io())
                .toObservable()
                .subscribeBy(
                        onNext = {
                            viewState.postValue(currentViewState().copy(items = it, showEmptyView = it.isEmpty(), showTitle = it.isNotEmpty()))
                        },
                        onError = {
                            viewState.postValue(currentViewState().copy(error = it.message))
                        }
                )
    }

    fun stop() {
        disposable?.dispose()
    }
}

data class ViewState(val isLoading: Boolean = false,
                     val items: List<PlaylistsOverviewModel>,
                     val error: String? = null,
                     val showEmptyView: Boolean = false,
                     val showTitle: Boolean = true)