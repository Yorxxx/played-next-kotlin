package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.FlowableUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.*
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class LoadGameUseCase @Inject constructor(
        private val grepository: GameRepository,
        private val comprepository: CompanyRepository,
        private val genre_repository: GenreRepository,
        private val collection_repository: CollectionRepository,
        private val platformRepository: PlatformRepository) : FlowableUseCaseWithParameter<Int, Game> {

    override fun execute(parameter: Int): Flowable<Game> {
        return grepository.load(parameter)
                .flatMap { loadDevelopers(it) }
                .flatMap { loadPublishers(it) }
                .flatMap { loadGenres(it) }
                .flatMap { loadCollection(it) }
                .flatMap { loadPlatforms(it) }
    }

    private fun loadDevelopers(game: Game): Flowable<Game> {
        return game.developers?.let { Flowable.just(game) }
                ?: comprepository.loadDevelopersForGameId(game.id)
                .onErrorReturn { listOf() }
                .map {
                    game.developers = it.takeIf { !it.isEmpty() }
                    game
                }.toFlowable()
    }

    private fun loadPublishers(game: Game): Flowable<Game> {
        return game.publishers?.let { Flowable.just(game) }
                ?: comprepository.loadPublishersForGame(game.id)
                .onErrorReturn { listOf() }
                .map {
                    game.publishers = it.takeIf { !it.isEmpty() }
                    game
                }.toFlowable()
    }

    private fun loadGenres(game: Game): Flowable<Game> {
        return game.genres?.let { Flowable.just(game) }
                ?: genre_repository.loadForGame(game.id)
                .onErrorReturn { listOf() }
                .map {
                    game.genres = it.takeIf { !it.isEmpty() }
                    game
                }.toFlowable()
    }

    private fun loadCollection(game: Game): Flowable<Game> {
        return game.collection?.let { Flowable.just(game) }
                ?: collection_repository.loadForGame(game.id)
                .map {
                    game.collection = it
                    game
                }
                .onErrorResumeNext { Single.just(game) }
                .toFlowable()
    }

    private fun loadPlatforms(game: Game): Flowable<Game> {
        return game.platforms?.let { Flowable.just(game) }
                ?: platformRepository.loadForGame(game.id)
                .onErrorReturn { listOf() }
                .map {
                    game.platforms = it.takeIf { !it.isEmpty() }
                    game
                }.toFlowable()
    }
}