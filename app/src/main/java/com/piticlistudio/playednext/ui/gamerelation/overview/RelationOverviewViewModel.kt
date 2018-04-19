package com.piticlistudio.playednext.ui.gamerelation.overview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.piticlistudio.playednext.domain.interactor.relation.LoadRelationsWithStatusUseCase
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.ui.gamerelation.detail.ViewState
import com.piticlistudio.playednext.ui.gamerelation.overview.mapper.RelationOverviewModelMapper
import com.piticlistudio.playednext.ui.gamerelation.overview.model.RelationOverviewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function5
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * View model for displaying an overview of the relations
 */
class RelationOverviewViewModel @Inject constructor(private val useCase: LoadRelationsWithStatusUseCase,
                                                    private val mapper: RelationOverviewModelMapper) : ViewModel() {

    private var disposable: Disposable? = null
    private val loading = MutableLiveData<Boolean>()
    fun getLoading(): LiveData<Boolean> = loading
    private val error = MutableLiveData<String>()
    fun getError(): LiveData<String> = error
    private val overview = MutableLiveData<List<RelationOverviewModel>>()
    fun getOverview(): LiveData<List<RelationOverviewModel>> = overview

    fun start() {
        disposable = Flowable.combineLatest(requestModelWithStatus(GameRelationStatus.UNPLAYED),
                requestModelWithStatus(GameRelationStatus.PLAYING),
                requestModelWithStatus(GameRelationStatus.PLAYED),
                requestModelWithStatus(GameRelationStatus.BEATEN),
                requestModelWithStatus(GameRelationStatus.COMPLETED),
                Function5<RelationOverviewModel, RelationOverviewModel, RelationOverviewModel, RelationOverviewModel, RelationOverviewModel, List<RelationOverviewModel>> { t1, t2, t3, t4, t5 ->
                    listOf(t1, t2, t3, t4, t5)
                })
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

    private fun requestModelWithStatus(status: GameRelationStatus): Flowable<RelationOverviewModel> {
        return useCase.execute(status)
                .map { mapper.mapIntoPresentationModel(it, status) }
    }
}