package com.piticlistudio.playednext.domain.interactor.relation

import com.piticlistudio.playednext.domain.interactor.FlowableUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import io.reactivex.Flowable
import javax.inject.Inject

class LoadRelationsForGameUseCase @Inject constructor(private val gameRelationRepository: GameRelationRepository) : FlowableUseCaseWithParameter<Game, List<GameRelation>> {

    override fun execute(parameter: Game): Flowable<List<GameRelation>> {
        return Flowable.fromIterable(parameter.platforms)
                .flatMapSingle { platform: Platform ->
                    gameRelationRepository.loadForGameAndPlatform(parameter.id, platform.id)
                            .firstOrError()
                            .map {
                                it.apply {
                                    this.game = parameter
                                    this.platform = platform
                                }
                            }
                }
                .toList()
                .toFlowable()
    }
}