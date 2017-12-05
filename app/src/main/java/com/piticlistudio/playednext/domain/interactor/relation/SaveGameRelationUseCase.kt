package com.piticlistudio.playednext.domain.interactor.relation

import com.piticlistudio.playednext.domain.interactor.CompletableUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import javax.inject.Inject

/**
 * Usecase for saving game relation changes
 */
class SaveGameRelationUseCase @Inject constructor(private val repository: GameRelationRepository) : CompletableUseCaseWithParameter<GameRelation> {

    override fun execute(parameter: GameRelation) = repository.save(parameter)
}