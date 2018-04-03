package com.piticlistudio.playednext.domain.interactor.game

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.domain.repository.*
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertNotNull

internal class SaveGameUseCaseTest {

    @Nested
    @DisplayName("Given SaveGameUseCase instance")
    inner class SaveGameUseCaseInstance {

        @Mock
        private lateinit var repository: GameRepository
          @Mock
        private lateinit var genreRepository: GenreRepository
        @Mock
        private lateinit var collectionRepository: CollectionRepository
        @Mock
        private lateinit var platformRepository: PlatformRepository
        @Mock
        private lateinit var imagesRepository: GameImagesRepository
        private var usecase: SaveGameUseCase? = null

        val game = makeGame()

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            whenever(repository.save(game)).thenReturn(Completable.complete())
            whenever(genreRepository.saveForGame(any(), any())).thenReturn(Completable.complete())
            whenever(platformRepository.saveForGame(any(), any())).thenReturn(Completable.complete())
            whenever(imagesRepository.save(any(), any())).thenReturn(Completable.complete())
            usecase = SaveGameUseCase(repository, genreRepository, collectionRepository, platformRepository, imagesRepository)
        }

        @Nested
        @DisplayName("When execute is called")
        inner class ExecuteIsCalled {

            var observer: TestObserver<Void>? = null

            @Nested
            @DisplayName("and does have developers")
            inner class WithDevelopers {

                @BeforeEach
                internal fun setUp() {
                    observer = usecase?.execute(game)?.test()
                }

                @Test
                @DisplayName("Then saves into repository")
                fun requestsRepository() {
                    verify(repository).save(game)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun emits() {
                    assertNotNull(observer)
                    with(observer) {
                        this!!.assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and does have genres")
            inner class WithGenres {

                @BeforeEach
                internal fun setUp() {
                    observer = usecase?.execute(game)?.test()
                }

                @Test
                @DisplayName("Then saves into repository")
                fun requestsRepository() {
                    verify(repository).save(game)
                }

                @Test
                @DisplayName("Then saves genres")
                fun savesDevelopers() {
                    verify(genreRepository).saveForGame(game.id, game.genres!!)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun emits() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and does not have genres")
            inner class WithoutGenres {

                var observer: TestObserver<Void>? = null

                @BeforeEach
                internal fun setUp() {
                    game.genres = null
                    observer = usecase?.execute(game)?.test()
                }

                @Test
                @DisplayName("Then does not save genres")
                fun doesNotRequestsRepository() {
                    verify(genreRepository, never()).saveForGame(anyInt(), any())
                }

                @Test
                @DisplayName("Then emits without errors")
                fun emits() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and does have collection")
            inner class WithCollection {

                @BeforeEach
                internal fun setUp() {
                    observer = usecase?.execute(game)?.test()
                }

                @Test
                @DisplayName("Then saves into repository")
                fun requestsRepository() {
                    verify(repository).save(game)
                }

                @Test
                @DisplayName("Then saves collection")
                fun savesDevelopers() {
                    verify(collectionRepository).saveForGame(game.id, game.collection!!)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun emits() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and does not collection")
            inner class WithoutCollection {

                var observer: TestObserver<Void>? = null

                @BeforeEach
                internal fun setUp() {
                    game.collection = null
                    observer = usecase?.execute(game)?.test()
                }

                @Test
                @DisplayName("Then does not save collection")
                fun doesNotRequestsRepository() {
                    verifyZeroInteractions(collectionRepository)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun emits() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and does have platforms")
            inner class WithPlatforms {

                @BeforeEach
                internal fun setUp() {
                    observer = usecase?.execute(game)?.test()
                }

                @Test
                @DisplayName("Then saves into repository")
                fun requestsRepository() {
                    verify(repository).save(game)
                }

                @Test
                @DisplayName("Then saves platforms")
                fun savesPlatforms() {
                    verify(platformRepository).saveForGame(game.id, game.platforms!!)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun emits() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and does not have platforms")
            inner class WithoutPlatforms {

                var observer: TestObserver<Void>? = null

                @BeforeEach
                internal fun setUp() {
                    game.platforms = null
                    observer = usecase?.execute(game)?.test()
                }

                @Test
                @DisplayName("Then does not save platforms")
                fun doesNotRequestsRepository() {
                    verify(platformRepository, never()).saveForGame(anyInt(), any())
                }

                @Test
                @DisplayName("Then emits without errors")
                fun emits() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and does have images")
            inner class WithImages {

                @BeforeEach
                internal fun setUp() {
                    observer = usecase?.execute(game)?.test()
                }

                @Test
                @DisplayName("Then saves into repository")
                fun requestsRepository() {
                    verify(repository).save(game)
                }

                @Test
                @DisplayName("Then saves developers")
                fun savesDevelopers() {
                    verify(imagesRepository).save(game.id, game.images!!)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun emits() {
                    assertNotNull(observer)
                    with(observer) {
                        this!!.assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }
        }
    }
}