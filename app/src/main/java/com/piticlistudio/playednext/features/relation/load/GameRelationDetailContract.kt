package com.piticlistudio.playednext.features.relation.load

import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.features.base.MvPPresenter
import com.piticlistudio.playednext.features.base.MvpView

interface GameRelationDetailContract {

    interface View : MvpView {
        fun showLoading()
        fun hideLoading()
        fun showData(data: GameRelation)
        fun hideData()
        fun showError(error: Throwable)
        fun hideError();
        fun load(id: Int)
    }

    interface Presenter : MvPPresenter<View> {
        fun load(gameId: Int, platformId: Int)
        fun save(data: GameRelation)
    }
}