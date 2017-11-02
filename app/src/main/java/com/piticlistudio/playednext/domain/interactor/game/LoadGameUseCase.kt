package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.SingleUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.*
import io.reactivex.Single
import javax.inject.Inject

class LoadGameUseCase @Inject constructor(
        private val grepository: GameRepository,
        private val comprepository: CompanyRepository,
        private val genre_repository: GenreRepository,
        private val collection_repository: CollectionRepository,
        private val platformRepository: PlatformRepository) : SingleUseCaseWithParameter<Int, Game> {

    override fun execute(parameter: Int): Single<Game> {
        return grepository.load(parameter)
                .flatMap { loadDevelopers(it) }
                .flatMap { loadPublishers(it) }
                .flatMap { loadGenres(it) }
                .flatMap { loadCollection(it) }
                .flatMap { loadPlatforms(it) }
    }

    private fun loadDevelopers(game: Game): Single<Game> {
        return game.developers?.let { Single.just(game) }
                ?: comprepository.loadDevelopersForGameId(game.id)
                .onErrorReturn { listOf() }
                .map {
                    game.developers = it.takeIf { !it.isEmpty() }
                    game
                }
    }

    private fun loadPublishers(game: Game): Single<Game> {
        return game.publishers?.let { Single.just(game) }
                ?: comprepository.loadPublishersForGame(game.id)
                .onErrorReturn { listOf() }
                .map {
                    game.publishers = it.takeIf { !it.isEmpty() }
                    game
                }
    }

    private fun loadGenres(game: Game): Single<Game> {
        return game.genres?.let { Single.just(game) }
                ?: genre_repository.loadForGame(game.id)
                .onErrorReturn { listOf() }
                .map {
                    game.genres = it.takeIf { !it.isEmpty() }
                    game
                }
    }

    private fun loadCollection(game: Game): Single<Game> {
        return game.collection?.let { Single.just(game) }
                ?: collection_repository.loadForGame(game.id)
                .map {
                    game.collection = it
                    game
                }
                .onErrorResumeNext { Single.just(game) }
    }

    private fun loadPlatforms(game: Game): Single<Game> {
        return game.platforms?.let { Single.just(game) }
                ?: platformRepository.loadForGame(game.id)
                .onErrorReturn { listOf() }
                .map {
                    game.platforms = it.takeIf { !it.isEmpty() }
                    game
                }
    }
}