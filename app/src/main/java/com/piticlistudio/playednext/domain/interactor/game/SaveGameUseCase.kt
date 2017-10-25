package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.CompletableUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.CompanyRepository
import com.piticlistudio.playednext.domain.repository.GameRepository
import io.reactivex.Completable

class SaveGameUseCase constructor(
        private val repository: GameRepository,
        private val comprepository: CompanyRepository) : CompletableUseCaseWithParameter<Game> {

    override fun execute(parameter: Game): Completable {
        return repository.save(parameter)
                .andThen(
                    if (parameter.developers != null) {
                        comprepository.saveDevelopersForGame(parameter.id, parameter.developers!!)
                    } else {
                        Completable.complete()
                    }
                )
                .andThen(
                    if (parameter.publishers != null) {
                        comprepository.savePublishersForGame(parameter.id, parameter.publishers!!)
                    } else {
                        Completable.complete()
                    }
                )
    }
}