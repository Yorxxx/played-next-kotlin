package com.piticlistudio.playednext.domain.interactor.game

import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.*
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompany
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGameImage
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenre
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subscribers.TestSubscriber
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
        @Mock lateinit var imagesRepository: GameImagesRepository

        private val gameId = 10
        private var useCase: LoadGameUseCase? = null

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            useCase = LoadGameUseCase(repository, companyrepository, genrerepository, collectionrepository, platformRepository, imagesRepository)
        }

        @Nested
        @DisplayName("When execute is called")
        inner class ExecuteIsCalled {

            private var testObserver: TestSubscriber<Game>? = null
            val result = makeGame()
            val companyList = randomListOf(factory = ::makeCompany)
            val genreList = randomListOf { makeGenre() }
            val collection = makeCollection()
            val platforms = randomListOf { makePlatform() }
            val images = randomListOf { makeGameImage() }

            @BeforeEach
            internal fun setup() {
                val flowable = Flowable.create<Game>({ it.onNext(result) }, BackpressureStrategy.MISSING)
                whenever(repository.load(gameId)).thenReturn(flowable)
                whenever(companyrepository.loadDevelopersForGameId(result.id)).thenReturn(Single.just(companyList))
                whenever(companyrepository.loadPublishersForGame(result.id)).thenReturn(Single.just(companyList))
                whenever(genrerepository.loadForGame(result.id)).thenReturn(Single.just(genreList))
                whenever(collectionrepository.loadForGame(result.id)).thenReturn(Single.just(collection))
                whenever(platformRepository.loadForGame(result.id)).thenReturn(Single.just(platforms))
                whenever(imagesRepository.loadForGame(result.id)).thenReturn(Flowable.just(images))
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
                testObserver?.apply {
                    assertNoErrors()
                    assertNotComplete()
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

            @Test
            @DisplayName("Then should not request images")
            fun noImagesRequested() {
                verify(imagesRepository, never()).loadForGame(gameId)
            }

            @Nested
            @DisplayName("And has empty developers")
            inner class WithoutDevelopers {

                @BeforeEach
                internal fun setUp() {
                    result.developers = listOf()
                    testObserver = useCase?.execute(gameId)!!.test()
                }

                @Test
                @DisplayName("Then does not requests company repository")
                fun requestsRepository() {
                    verify(companyrepository, never()).loadDevelopersForGameId(result.id)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun withoutErrors() {
                    assertNotNull(testObserver)
                    testObserver?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue { it.developers != null && it.developers!!.isEmpty() }
                    }
                }
            }

            @Nested
            @DisplayName("And does not have developers assigned")
            inner class WithNullDevelopers {

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
                    testObserver?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue { it.developers == companyList }
                    }
                }

                @Nested
                @DisplayName("And request fails")
                inner class RequestFails {

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
                        testObserver?.apply {
                            assertNoErrors()
                            assertNotComplete()
                            assertValueCount(1)
                            assertValue { it.developers != null && it.developers!!.isEmpty() }
                        }
                    }
                }
            }

            @Nested
            @DisplayName("And has empty publishers")
            inner class EmptyPublishers {

                @BeforeEach
                internal fun setUp() {
                    result.publishers = listOf()
                    testObserver = useCase?.execute(gameId)!!.test()
                }

                @Test
                @DisplayName("Then does not requests company repository")
                fun requestsRepository() {
                    verify(companyrepository, never()).loadPublishersForGame(result.id)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun withoutErrors() {
                    assertNotNull(testObserver)
                    testObserver?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue { it.publishers != null && it.publishers!!.isEmpty() }
                    }
                }
            }

            @Nested
            @DisplayName("And does not have publishers assigned")
            inner class WithoutPublishers {

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
                    testObserver?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue { it.publishers == companyList }
                    }
                }

                @Nested
                @DisplayName("And request fails")
                inner class RequestFails {

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
                        testObserver?.apply {
                            assertNoErrors()
                            assertValueCount(1)
                            assertNotComplete()
                            assertValue { it.publishers != null && it.publishers!!.isEmpty() }
                        }
                    }
                }
            }

            @Nested
            @DisplayName("And has empty genres")
            inner class EmptyGenres {

                @BeforeEach
                internal fun setUp() {
                    result.genres = listOf()
                    testObserver = useCase?.execute(gameId)!!.test()
                }

                @Test
                @DisplayName("Then does not requests genre repository")
                fun requestsRepository() {
                    verify(genrerepository, never()).loadForGame(result.id)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun withoutErrors() {
                    assertNotNull(testObserver)
                    testObserver?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue { it.genres != null && it.genres!!.isEmpty() }
                    }
                }
            }

            @Nested
            @DisplayName("And does not have genres assigned")
            inner class WithoutGenres {

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
                    testObserver?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue { it.genres == genreList }
                    }
                }

                @Nested
                @DisplayName("And request fails")
                inner class RequestFails {

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
                        testObserver?.apply {
                            assertNoErrors()
                            assertNotComplete()
                            assertValueCount(1)
                            assertValue { it.genres != null && it.genres!!.isEmpty() }
                        }
                    }
                }
            }

            @Nested
            @DisplayName("And does not have collection assigned")
            inner class WithoutCollection {

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
                    testObserver?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue { it.collection == collection }
                    }
                }

                @Nested
                @DisplayName("And request fails")
                inner class RequestFails {

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
                        testObserver?.apply {
                            assertNoErrors()
                            assertNotComplete()
                            assertValueCount(1)
                            assertValue { it.collection == null }
                        }
                    }
                }
            }

            @Nested
            @DisplayName("And has empty platforms")
            inner class EmptyPlatforms {

                @BeforeEach
                internal fun setUp() {
                    result.platforms = listOf()
                    testObserver = useCase?.execute(gameId)!!.test()
                }

                @Test
                @DisplayName("Then does not requests platform repository")
                fun requestsRepository() {
                    verify(platformRepository, never()).loadForGame(result.id)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun withoutErrors() {
                    assertNotNull(testObserver)
                    testObserver?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue { it.platforms != null && it.platforms!!.isEmpty() }
                    }
                }
            }

            @Nested
            @DisplayName("And does not have platforms assigned")
            inner class WithoutPlatforms {

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
                    testObserver?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue { it.platforms == platforms }
                    }
                }

                @Nested
                @DisplayName("And request fails")
                inner class RequestFails {

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
                        testObserver?.apply {
                            assertNoErrors()
                            assertNotComplete()
                            assertValueCount(1)
                            assertValue { it.platforms != null && it.platforms!!.isEmpty() }
                        }
                    }
                }
            }

            @Nested
            @DisplayName("And has empty images")
            inner class EmptyImages {

                @BeforeEach
                internal fun setUp() {
                    result.images = listOf()
                    testObserver = useCase?.execute(gameId)!!.test()
                }

                @Test
                @DisplayName("Then does not requests images repository")
                fun requestsRepository() {
                    verify(imagesRepository, never()).loadForGame(result.id)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun withoutErrors() {
                    assertNotNull(testObserver)
                    testObserver?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue { it.images != null && it.images!!.isEmpty() }
                    }
                }
            }

            @Nested
            @DisplayName("And does not have images assigned")
            inner class WithoutImages {

                @BeforeEach
                internal fun setUp() {
                    result.images = null
                    testObserver = useCase?.execute(gameId)!!.test()
                }

                @Test
                @DisplayName("Then requests repository")
                fun requestsRepository() {
                    verify(imagesRepository).loadForGame(result.id)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun withoutErrors() {
                    assertNotNull(testObserver)
                    testObserver?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue { it.images == images }
                    }
                }

                @Nested
                @DisplayName("And request fails")
                inner class RequestFails {

                    @BeforeEach
                    internal fun setUp() {
                        result.images = null
                        whenever(imagesRepository.loadForGame(anyInt())).thenReturn(Flowable.error(Throwable("foo")))
                        testObserver = useCase?.execute(gameId)!!.test()
                    }

                    @Test
                    @DisplayName("Then emits ignores errors")
                    fun withoutErrors() {
                        assertNotNull(testObserver)
                        testObserver?.apply {
                            assertNoErrors()
                            assertNotComplete()
                            assertValueCount(1)
                            assertValue { it.images != null && it.images!!.isEmpty() }
                        }
                    }
                }
            }
        }
    }
}