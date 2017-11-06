package com.piticlistudio.playednext.features.gamerelation.load

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.piticlistudio.playednext.domain.interactor.relation.LoadGameRelationUseCase
import com.piticlistudio.playednext.domain.model.GameRelation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameRelationLoadViewModel : ViewModel() {

    @Inject
    lateinit var loadRelationUseCase: LoadGameRelationUseCase

    var relation: MutableLiveData<GameRelation?> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()
    var error: MutableLiveData<Throwable?> = MutableLiveData()

    fun loadRelation() {
        loadRelationUseCase.execute(Pair(10, 20))
                .doOnSubscribe { loading.postValue(true) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .doOnTerminate { loading.postValue(false) }
                .subscribeBy(
                        onNext = {
                            relation.postValue(it)
                            error.postValue(null)
                        },
                        onError = {
                            error.postValue(it)
                            relation.postValue(null)
                        }
                )
    }

}