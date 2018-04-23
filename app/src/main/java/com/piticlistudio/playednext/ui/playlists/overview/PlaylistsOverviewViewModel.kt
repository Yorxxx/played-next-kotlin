package com.piticlistudio.playednext.ui.playlists.overview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.piticlistudio.playednext.domain.interactor.playlists.LoadAllPlaylistsUseCase
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
    private val loading = MutableLiveData<Boolean>()
    fun getLoading(): LiveData<Boolean> = loading
    private val error = MutableLiveData<String>()
    fun getError(): LiveData<String> = error
    private val overview = MutableLiveData<List<PlaylistsOverviewModel>>()
    fun getOverview(): LiveData<List<PlaylistsOverviewModel>> = overview

    fun start() {
        disposable = useCase.execute()
                .flatMapSingle {
                    Flowable.fromIterable(it)
                            .map { mapper.mapIntoPresentationModel(it) }
                            .toList()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    loading.postValue(true)
                    error.postValue(null)
                }
                .doOnEach {
                    loading.postValue(false)
                }
                .subscribeOn(Schedulers.io())
                .toObservable()
                .subscribeBy(
                        onNext = { overview.postValue(it) },
                        onError = { error.postValue(it.message) }
                )
    }

    fun stop() {
        disposable?.dispose()
    }
}