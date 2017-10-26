package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.CompletableUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.CollectionRepository
import com.piticlistudio.playednext.domain.repository.CompanyRepository
import com.piticlistudio.playednext.domain.repository.GameRepository
import com.piticlistudio.playednext.domain.repository.GenreRepository
import io.reactivex.Completable

class SaveGameUseCase constructor(
        private val repository: GameRepository,
        private val comprepository: CompanyRepository,
        private val genrerepository: GenreRepository,
        private val collectionRepository: CollectionRepository) : CompletableUseCaseWithParameter<Game> {

    override fun execute(parameter: Game): Completable {
        return repository.save(parameter)
                .andThen(Completable.concat(listOf(
                        saveDevelopers(parameter),
                        savePublishers(parameter),
                        saveGenres(parameter),
                        saveCollection(parameter))))
    }

    private fun saveDevelopers(game: Game): Completable {
        return game.developers?.let {
            comprepository.saveDevelopersForGame(game.id, game.developers!!)
        } ?: Completable.complete()
    }

    private fun savePublishers(game: Game): Completable {
        return game.publishers?.let {
            comprepository.savePublishersForGame(game.id, game.publishers!!)
        } ?: Completable.complete()
    }

    private fun saveGenres(game: Game): Completable {
        return game.genres?.let {
            genrerepository.saveForGame(game.id, game.genres!!)
        } ?: Completable.complete()
    }

    private fun saveCollection(game: Game): Completable {
        return game.collection?.let { collectionRepository.saveForGame(game.id, game.collection!!) } ?: Completable.complete()
    }
}