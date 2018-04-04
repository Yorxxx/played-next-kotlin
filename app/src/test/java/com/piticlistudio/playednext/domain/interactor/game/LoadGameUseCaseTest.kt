package com.piticlistudio.playednext.domain.interactor.game

import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.interactor.platform.LoadPlatformsForGameUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.CollectionRepository
import com.piticlistudio.playednext.domain.repository.GameImagesRepository
import com.piticlistudio.playednext.domain.repository.GameRepository
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

        @Mock
        lateinit var repository: GameRepository
        @Mock
        lateinit var collectionrepository: CollectionRepository
        @Mock
        lateinit var platformRepository: LoadPlatformsForGameUseCase
        @Mock
        lateinit var imagesRepository: GameImagesRepository

        private val gameId = 10
        private var useCase: LoadGameUseCase? = null

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            useCase = LoadGameUseCase(repository, collectionrepository, platformRepository, imagesRepository)
        }

        @Nested
        @DisplayName("When execute is called")
        inner class ExecuteIsCalled {

            private var testObserver: TestSubscriber<Game>? = null
            val result = makeGame()
            val companyList = randomListOf(10) { makeCompany() }
            val genreList = randomListOf(4) { makeGenre() }
            val collection = makeCollection()
            val platforms = randomListOf(5) { makePlatform() }
            val images = randomListOf(10) { makeGameImage() }

            @BeforeEach
            internal fun setup() {
                val flowable = Flowable.create<Game>({ it.onNext(result) }, BackpressureStrategy.MISSING)
                whenever(repository.load(gameId)).thenReturn(flowable)
                whenever(collectionrepository.loadForGame(result.id)).thenReturn(Single.just(collection))
                whenever(platformRepository.execute(result)).thenReturn(Single.just(platforms))
                whenever(imagesRepository.loadForGame(result.id)).thenReturn(Flowable.just(images))
                testObserver = useCase?.execute(gameId)!!.test()
            }

            @Test
            @DisplayName("Then requests repository")
            fun requestsRepository() {
                verify(repository).load(gameId)
            }

            @Test
            @DisplayName("Then requests platforms")
            fun requestPlatforms() {
                verify(platformRepository).execute(result)
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
            @DisplayName("Then should not request collection")
            fun noCollectionRequest() {
                verify(collectionrepository, never()).loadForGame(gameId)
            }

            @Test
            @DisplayName("Then should not request images")
            fun noImagesRequested() {
                verify(imagesRepository, never()).loadForGame(gameId)
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