package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.CompletableUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.*
import io.reactivex.Completable
import javax.inject.Inject

class SaveGameUseCase @Inject constructor(
        private val repository: GameRepository,
        private val genrerepository: GenreRepository,
        private val collectionRepository: CollectionRepository,
        private val platformRepository: PlatformRepository,
        private val imagesRepository: GameImagesRepository) : CompletableUseCaseWithParameter<Game> {

    override fun execute(parameter: Game): Completable {
        return repository.save(parameter)
                .andThen(Completable.concat(listOf(
                        saveGenres(parameter),
                        saveCollection(parameter),
                        savePlatforms(parameter),
                        saveImages(parameter))))
    }

    private fun saveGenres(game: Game): Completable {
        return game.genres?.let {
            genrerepository.saveForGame(game.id, it)
        } ?: Completable.complete()
    }

    private fun saveCollection(game: Game): Completable {
        return game.collection?.let { collectionRepository.saveForGame(game.id, it) } ?: Completable.complete()
    }

    private fun savePlatforms(game: Game): Completable {
        return game.platforms?.let {
            platformRepository.saveForGame(game.id, it)
        } ?: Completable.complete()
    }

    private fun saveImages(game: Game): Completable {
        return game.images?.let {
            imagesRepository.save(game.id, it)
        } ?: Completable.complete()
    }
}