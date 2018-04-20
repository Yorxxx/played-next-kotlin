package com.piticlistudio.playednext.domain.interactor.relation

import com.piticlistudio.playednext.domain.interactor.FlowableUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import io.reactivex.Flowable
import javax.inject.Inject

class LoadRelationsWithStatusUseCase @Inject constructor(private val repository: GameRelationRepository): FlowableUseCaseWithParameter<GameRelationStatus, List<GameRelation>> {

    override fun execute(parameter: GameRelationStatus): Flowable<List<GameRelation>> {
        return repository.loadWithStatus(parameter)
    }
}