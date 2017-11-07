package com.piticlistudio.playednext.features.gamerelation.load

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.piticlistudio.playednext.domain.interactor.relation.LoadGameRelationUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameRelationDetailViewModel @Inject constructor(private val loadGameRelationUseCase: LoadGameRelationUseCase) : ViewModel() {

    private var viewmodel: MutableLiveData<GameRelationDetailViewState>? = null

    fun loadRelation() {
        loadGameRelationUseCase.execute(Pair(10, 20))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    data, error ->  Log.d("FooActivity", "onNext called")
                }
    }

    fun getViewState(): LiveData<GameRelationDetailViewState> {
        if (viewmodel == null) {
            viewmodel = MutableLiveData<GameRelationDetailViewState>()
            loadRelation()
        }
        return viewmodel!!
    }
}