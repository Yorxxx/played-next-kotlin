package com.piticlistudio.playednext.ui.gamerelation.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.piticlistudio.playednext.domain.interactor.relation.LoadGameRelationUseCase
import com.piticlistudio.playednext.domain.model.GameRelation
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * ViewModel for GameRelationDetail view.
 */
class GameRelationDetailViewModel @Inject constructor(private val loadGameRelationUseCase: LoadGameRelationUseCase) : ViewModel() {

    private val loadingStatus = MutableLiveData<Boolean>()
    private val dataStatus = MutableLiveData<GameRelation?>()
    private val errorStatus = MutableLiveData<Throwable?>()
    fun getLoading(): LiveData<Boolean> = loadingStatus
    fun getData(): LiveData<GameRelation?> = dataStatus
    fun getError(): LiveData<Throwable?> = errorStatus

    fun loadRelation() {
        loadGameRelationUseCase.execute(Pair(2001, 9))
                .doOnSubscribe { loadingStatus.postValue(true) }
                .subscribeOn(Schedulers.io())
                .toObservable()
                .subscribeBy(
                        onNext = {
                            dataStatus.postValue(it)
                            loadingStatus.postValue(false)
                            errorStatus.postValue(null)
                        },
                        onError = {
                            dataStatus.postValue(null)
                            errorStatus.postValue(it)
                            loadingStatus.postValue(false)
                        }
                )
    }
}