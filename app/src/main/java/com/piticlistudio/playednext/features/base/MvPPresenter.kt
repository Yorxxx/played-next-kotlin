package com.piticlistudio.playednext.features.base

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
interface MvPPresenter<V : MvpView> {

    fun attachView(mvpView: V)

    fun detachView()
}
