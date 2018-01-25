package com.piticlistudio.playednext.domain.interactor.relation

import android.arch.persistence.room.EmptyResultSetException
import com.piticlistudio.playednext.databinding.GamerelationDetailBinding
import com.piticlistudio.playednext.domain.interactor.FlowableUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import rx.Observable
import javax.inject.Inject

class LoadRelationsForGameUseCase @Inject constructor(private val gameRelationRepository: GameRelationRepository) : FlowableUseCaseWithParameter<Game, List<GameRelation>> {

    override fun execute(parameter: Game): Flowable<List<GameRelation>> {
        return Flowable.fromIterable(parameter.platforms)
                .flatMapSingle { platform: Platform ->
                    gameRelationRepository.loadForGameAndPlatform(parameter.id, platform.id)
                            .onErrorResumeNext { t: Throwable ->
                                if (t is EmptyResultSetException) {
                                    Flowable.just(GameRelation(parameter, platform, GameRelationStatus.NONE, System.currentTimeMillis(), System.currentTimeMillis()))
                                } else {
                                    Flowable.error(t)
                                }
                            }
                            .firstOrError()
                }
                .toList()
                .toFlowable()
    }
}