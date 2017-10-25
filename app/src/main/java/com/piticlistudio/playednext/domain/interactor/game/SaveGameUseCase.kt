package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.CompletableUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.CompanyRepository
import com.piticlistudio.playednext.domain.repository.GameRepository
import com.piticlistudio.playednext.domain.repository.GenreRepository
import io.reactivex.Completable

class SaveGameUseCase constructor(
        private val repository: GameRepository,
        private val comprepository: CompanyRepository,
        private val genrerepository: GenreRepository) : CompletableUseCaseWithParameter<Game> {

    override fun execute(parameter: Game): Completable {
        return repository.save(parameter)
                .andThen(Completable.concat(listOf(
                        saveDevelopers(parameter),
                        savePublishers(parameter),
                        saveGenres(parameter))))
    }

    private fun saveDevelopers(game: Game): Completable {
        if (game.developers != null) {
            return comprepository.saveDevelopersForGame(game.id, game.developers!!)
        } else {
            return Completable.complete()
        }
    }

    private fun savePublishers(game: Game): Completable {
        if (game.publishers != null) {
            return comprepository.savePublishersForGame(game.id, game.publishers!!)
        } else {
            return Completable.complete()
        }
    }

    private fun saveGenres(game: Game): Completable {
        if (game.genres != null) {
            return genrerepository.saveForGame(game.id, game.genres!!)
        } else {
            return Completable.complete()
        }
    }
}