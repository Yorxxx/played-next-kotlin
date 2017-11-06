package com.piticlistudio.playednext.features.relation.load

import com.piticlistudio.playednext.domain.interactor.relation.LoadGameRelationUseCase
import com.piticlistudio.playednext.domain.interactor.relation.SaveGameRelationUseCase
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.features.base.BasePresenter
import javax.inject.Inject

class GameRelationDetailPresenter @Inject constructor(private val loadUseCase: LoadGameRelationUseCase,
                                                      private val saveUseCase: SaveGameRelationUseCase) : BasePresenter<GameRelationDetailContract.View>(), GameRelationDetailContract.Presenter {

    override fun load(gameId: Int, platformId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(data: GameRelation) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}