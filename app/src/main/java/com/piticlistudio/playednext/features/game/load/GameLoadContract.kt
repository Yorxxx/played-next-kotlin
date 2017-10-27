package com.piticlistudio.playednext.features.game.load

import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.features.base.MvPPresenter
import com.piticlistudio.playednext.features.base.MvpView

/**
 * Contract for feature loading a game
 */
interface GameLoadContract {

    interface View: MvpView {
        fun showLoading()
        fun hideLoading()
        fun showData(data: Game)
        fun hideData()
        fun showError(error: Throwable)
        fun hideError();
        fun load(id: Int)
    }
    interface Presenter: MvPPresenter<View> {
        fun load(id: Int)
    }
}