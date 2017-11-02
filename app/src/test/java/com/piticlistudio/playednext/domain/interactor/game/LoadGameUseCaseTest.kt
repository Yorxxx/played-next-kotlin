package com.piticlistudio.playednext.domain.interactor.game

import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.*
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyList
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenreList
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatformList
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LoadGameUseCaseTest {

    @Nested
    @DisplayName("Given a LoadGameUseCase instance")
    inner class LoadGameUseCaseInstance {

        @Mock lateinit var repository: GameRepository
        @Mock lateinit var companyrepository: CompanyRepository
        @Mock lateinit var genrerepository: GenreRepository
        @Mock lateinit var collectionrepository: CollectionRepository
        @Mock lateinit var platformRepository: PlatformRepository

        private val gameId = 10
        private var useCase: LoadGameUseCase? = null

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            useCase = LoadGameUseCase(repository, companyrepository, genrerepository, collectionrepository, platformRepository)
        }

        @Nested
        @DisplayName("When execute is called")
        inner class executeIsCalled {

            private var testObserver: TestObserver<Game>? = null
            val result = makeGame()
            val companyList = makeCompanyList()
            val genreList = makeGenreList()
            val collection = makeCollection()
            val platforms = makePlatformList()

            @BeforeEach
            internal fun setup() {
                whenever(repository.load(gameId)).thenReturn(Single.just(result))
                whenever(companyrepository.loadDevelopersForGameId(result.id)).thenReturn(Single.just(companyList))
                whenever(companyrepository.loadPublishersForGame(result.id)).thenReturn(Single.just(companyList))
                whenever(genrerepository.loadForGame(result.id)).thenReturn(Single.just(genreList))
                whenever(collectionrepository.loadForGame(result.id)).thenReturn(Single.just(collection))
                whenever(platformRepository.loadForGame(result.id)).thenReturn(Single.just(platforms))
                testObserver = useCase?.execute(gameId)!!.test()
            }

            @Test
            @DisplayName("Then requests repository")
            fun requestsRepository() {
                verify(repository).load(gameId)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(testObserver)
                with(testObserver) {
                    this!!.assertNoErrors()
                    assertComplete()
                    assertValue(result)
                }
            }

            @Test
            @DisplayName("Then should not request developers")
            fun noDevsRequest() {
                verifyZeroInteractions(companyrepository)
            }

            @Test
            @DisplayName("Then should not request publishers")
            fun noPublishersRequest() {
                verify(companyrepository, never()).loadPublishersForGame(gameId)
            }

            @Test
            @DisplayName("Then should not request genres")
            fun noGenresRequest() {
                verify(genrerepository, never()).loadForGame(gameId)
            }

            @Test
            @DisplayName("Then should not request collection")
            fun noCollectionRequest() {
                verify(collectionrepository, never()).loadForGame(gameId)
            }

            @Test
            @DisplayName("Then should not request platforms")
            fun noPlatformsRequest() {
                verify(platformRepository, never()).loadForGame(gameId)
            }

            @Nested
            @DisplayName("And does not have developers assigned")
            inner class withoutDevelopers {

                @BeforeEach
                internal fun setUp() {
                    result.developers = null
                    testObserver = useCase?.execute(gameId)!!.test()
                }

                @Test
                @DisplayName("Then requests company repository")
                fun requestsRepository() {
                    verify(companyrepository).loadDevelopersForGameId(result.id)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun withoutErrors() {
                    assertNotNull(testObserver)
                    with(testObserver) {
                        this!!.assertNoErrors()
                        assertComplete()
                        assertValue {
                            it.developers == companyList
                        }
                    }
                }

                @Nested
                @DisplayName("And request fails")
                inner class requestFails {

                    @BeforeEach
                    internal fun setUp() {
                        result.developers = null
                        whenever(companyrepository.loadDevelopersForGameId(anyInt())).thenReturn(Single.error(Throwable("foo")))
                        testObserver = useCase?.execute(gameId)!!.test()
                    }

                    @Test
                    @DisplayName("Then emits ignores errors")
                    fun withoutErrors() {
                        assertNotNull(testObserver)
                        with(testObserver) {
                            this!!.assertNoErrors()
                            assertComplete()
                            assertValueCount(1)
                            assertValue {
                                it.developers == null
                            }
                        }
                    }
                }
            }

            @Nested
            @DisplayName("And does not have publishers assigned")
            inner class withoutPublishers {

                @BeforeEach
                internal fun setUp() {
                    result.publishers = null
                    testObserver = useCase?.execute(gameId)!!.test()
                }

                @Test
                @DisplayName("Then requests company repository")
                fun requestsRepository() {
                    verify(companyrepository).loadPublishersForGame(result.id)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun withoutErrors() {
                    assertNotNull(testObserver)
                    with(testObserver) {
                        this!!.assertNoErrors()
                        assertComplete()
                        assertValueCount(1)
                        assertValue {
                            it.publishers == companyList
                        }
                    }
                }

                @Nested
                @DisplayName("And request fails")
                inner class requestFails {

                    @BeforeEach
                    internal fun setUp() {
                        result.publishers = null
                        whenever(companyrepository.loadPublishersForGame(anyInt())).thenReturn(Single.error(Throwable("foo")))
                        testObserver = useCase?.execute(gameId)!!.test()
                    }

                    @Test
                    @DisplayName("Then emits ignores errors")
                    fun withoutErrors() {
                        assertNotNull(testObserver)
                        with(testObserver) {
                            this!!.assertNoErrors()
                            assertValueCount(1)
                            assertComplete()
                            assertValue {
                                it.publishers == null
                            }
                        }
                    }
                }
            }

            @Nested
            @DisplayName("And does not have genres assigned")
            inner class withoutGenres {

                @BeforeEach
                internal fun setUp() {
                    result.genres = null
                    testObserver = useCase?.execute(gameId)!!.test()
                }

                @Test
                @DisplayName("Then requests repository")
                fun requestsRepository() {
                    verify(genrerepository).loadForGame(result.id)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun withoutErrors() {
                    assertNotNull(testObserver)
                    with(testObserver) {
                        this!!.assertNoErrors()
                        assertComplete()
                        assertValueCount(1)
                        assertValue {
                            it.genres == genreList
                        }
                    }
                }

                @Nested
                @DisplayName("And request fails")
                inner class requestFails {

                    @BeforeEach
                    internal fun setUp() {
                        result.genres = null
                        whenever(genrerepository.loadForGame(anyInt())).thenReturn(Single.error(Throwable("foo")))
                        testObserver = useCase?.execute(gameId)!!.test()
                    }

                    @Test
                    @DisplayName("Then emits ignores errors")
                    fun withoutErrors() {
                        assertNotNull(testObserver)
                        with(testObserver) {
                            this!!.assertNoErrors()
                            assertValueCount(1)
                            assertComplete()
                            assertValue {
                                it.genres == null
                            }
                        }
                    }
                }
            }

            @Nested
            @DisplayName("And does not have collection assigned")
            inner class withoutCollection {

                @BeforeEach
                internal fun setUp() {
                    result.collection = null
                    testObserver = useCase?.execute(gameId)!!.test()
                }

                @Test
                @DisplayName("Then requests repository")
                fun requestsRepository() {
                    verify(collectionrepository).loadForGame(result.id)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun withoutErrors() {
                    assertNotNull(testObserver)
                    with(testObserver) {
                        this!!.assertNoErrors()
                        assertComplete()
                        assertValueCount(1)
                        assertValue {
                            it.collection == collection
                        }
                    }
                }

                @Nested
                @DisplayName("And request fails")
                inner class requestFails {

                    @BeforeEach
                    internal fun setUp() {
                        result.collection = null
                        whenever(collectionrepository.loadForGame(result.id)).thenReturn(Single.error(Throwable("foo")))
                        testObserver = useCase?.execute(gameId)!!.test()
                    }

                    @Test
                    @DisplayName("Then emits ignores errors")
                    fun withoutErrors() {
                        assertNotNull(testObserver)
                        with(testObserver) {
                            this!!.assertNoErrors()
                            assertValueCount(1)
                            assertComplete()
                            assertValue {
                                it.collection == null
                            }
                        }
                    }
                }
            }

            @Nested
            @DisplayName("And does not have platforms assigned")
            inner class withoutPlatforms {

                @BeforeEach
                internal fun setUp() {
                    result.platforms = null
                    testObserver = useCase?.execute(gameId)!!.test()
                }

                @Test
                @DisplayName("Then requests repository")
                fun requestsRepository() {
                    verify(platformRepository).loadForGame(result.id)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun withoutErrors() {
                    assertNotNull(testObserver)
                    with(testObserver) {
                        this!!.assertNoErrors()
                        assertComplete()
                        assertValueCount(1)
                        assertValue {
                            it.platforms == platforms
                        }
                    }
                }

                @Nested
                @DisplayName("And request fails")
                inner class requestFails {

                    @BeforeEach
                    internal fun setUp() {
                        result.platforms = null
                        whenever(platformRepository.loadForGame(anyInt())).thenReturn(Single.error(Throwable("foo")))
                        testObserver = useCase?.execute(gameId)!!.test()
                    }

                    @Test
                    @DisplayName("Then emits ignores errors")
                    fun withoutErrors() {
                        assertNotNull(testObserver)
                        with(testObserver) {
                            this!!.assertNoErrors()
                            assertValueCount(1)
                            assertComplete()
                            assertValue {
                                it.platforms == null
                            }
                        }
                    }
                }
            }
        }
    }
}