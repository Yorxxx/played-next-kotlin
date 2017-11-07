package com.piticlistudio.playednext.domain.interactor.relation

import com.piticlistudio.playednext.domain.interactor.FlowableUseCaseWithParameter
import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.platform.LoadPlatformUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class LoadGameRelationUseCase @Inject constructor(private val loadGameUseCase: LoadGameUseCase,
                                                  private val loadPlatformUseCase: LoadPlatformUseCase,
                                                  private val gameRelationRepository: GameRelationRepository) : FlowableUseCaseWithParameter<Pair<Int, Int>, GameRelation> {

    override fun execute(parameter: Pair<Int, Int>): Flowable<GameRelation> {
        return gameRelationRepository.loadForGameAndPlatform(parameter.first, parameter.second)
                .flatMapSingle {
                    Single.zip(loadGameUseCase.execute(parameter.first).firstOrError(), loadPlatformUseCase.execute(parameter.second), BiFunction { t1: Game, t2: Platform ->
                        it.apply {
                            game = t1
                            platform = t2
                        }
                    })
                }
    }
}