package com.piticlistudio.playednext.features.game.search

import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.features.base.MvPPresenter
import com.piticlistudio.playednext.features.base.MvpView

/**
 * Contract for the view implementing the search of a game
 */
interface GameSearchContract {

    interface View : MvpView {

        fun showLoading()
        fun hideLoading()
        fun showData(data: List<Game>)
        fun hideData()
        fun showError(error: Throwable)
        fun hideError();
        fun search(query: String)
    }

    interface Presenter : MvPPresenter<View> {
        fun search(query: String)
    }
}