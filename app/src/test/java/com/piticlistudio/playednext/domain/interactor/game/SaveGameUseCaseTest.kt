package com.piticlistudio.playednext.domain.interactor.game

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.domain.repository.CollectionRepository
import com.piticlistudio.playednext.domain.repository.CompanyRepository
import com.piticlistudio.playednext.domain.repository.GameRepository
import com.piticlistudio.playednext.domain.repository.GenreRepository
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
        private lateinit var companyRepository: CompanyRepository
        @Mock
        private lateinit var genreRepository: GenreRepository
        @Mock
        private lateinit var collectionRepository: CollectionRepository
        private var usecase: SaveGameUseCase? = null

        val game = makeGame()

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            whenever(repository.save(game)).thenReturn(Completable.complete())
            whenever(companyRepository.saveDevelopersForGame(any(), any())).thenReturn(Completable.complete())
            whenever(companyRepository.savePublishersForGame(any(), any())).thenReturn(Completable.complete())
            whenever(genreRepository.saveForGame(any(), any())).thenReturn(Completable.complete())
            usecase = SaveGameUseCase(repository, companyRepository, genreRepository, collectionRepository)
        }

        @Nested
        @DisplayName("When execute is called")
        inner class executeIsCalled {

            var observer: TestObserver<Void>? = null

            @Nested
            @DisplayName("and does have developers")
            inner class withDevelopers {

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
                    verify(companyRepository).saveDevelopersForGame(game.id, game.developers!!)
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
            @DisplayName("and does not have developers")
            inner class withoutDevelopers {

                var observer: TestObserver<Void>? = null

                @BeforeEach
                internal fun setUp() {
                    game.developers = null
                    observer = usecase?.execute(game)?.test()
                }

                @Test
                @DisplayName("Then does not save developers")
                fun doesNotRequestsRepository() {
                    verify(companyRepository, never()).saveDevelopersForGame(anyInt(), any())
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
            @DisplayName("and does have publishers")
            inner class withPublishers {

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
                @DisplayName("Then saves publishers")
                fun savesDevelopers() {
                    verify(companyRepository).savePublishersForGame(game.id, game.publishers!!)
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
            @DisplayName("and does not have publishers")
            inner class withoutPublishers {

                var observer: TestObserver<Void>? = null

                @BeforeEach
                internal fun setUp() {
                    game.publishers = null
                    observer = usecase?.execute(game)?.test()
                }

                @Test
                @DisplayName("Then does not save publishers")
                fun doesNotRequestsRepository() {
                    verify(companyRepository, never()).savePublishersForGame(anyInt(), any())
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
            inner class withGenres {

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
                    with(observer) {
                        this!!.assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and does not have genres")
            inner class withoutGenres {

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
                    with(observer) {
                        this!!.assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and does have collection")
            inner class withCollection {

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
                    with(observer) {
                        this!!.assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and does not collection")
            inner class withoutCollection {

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