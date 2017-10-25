package com.piticlistudio.playednext.domain.interactor.game

import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.CompanyRepository
import com.piticlistudio.playednext.domain.repository.GameRepository
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyList
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.BeforeClass
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoadGameUseCaseTest {

    @Nested
    @DisplayName("Given a LoadGameUseCase instance")
    inner class LoadGameUseCaseInstance {

        @Mock lateinit var repository: GameRepository
        @Mock lateinit var companyrepository: CompanyRepository

        private val gameId = 10;
        private var useCase: LoadGameUseCase? = null

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            useCase = LoadGameUseCase(repository, companyrepository)
        }

        @Nested
        @DisplayName("When execute is called")
        inner class executeIsCalled {

            private var testObserver: TestObserver<Game>? = null
            val result = makeGame()
            val companyList = makeCompanyList()

            @BeforeEach
            internal fun setup() {
                whenever(repository.load(gameId)).thenReturn(Single.just(result))
                whenever(companyrepository.loadDevelopersForGameId(gameId)).thenReturn(Single.just(companyList))
                whenever(companyrepository.loadPublishersForGame(gameId)).thenReturn(Single.just(companyList))
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
                verify(companyrepository, never()).loadPublishersForGame(gameId);
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
                    verify(companyrepository).loadDevelopersForGameId(gameId)
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
                        whenever(companyrepository.loadDevelopersForGameId(gameId)).thenReturn(Single.error(Throwable("foo")))
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
                    whenever(repository.load(gameId)).thenReturn(Single.just(result))
                    testObserver = useCase?.execute(gameId)!!.test()
                }

                @Test
                @DisplayName("Then requests company repository")
                fun requestsRepository() {
                    verify(companyrepository).loadPublishersForGame(gameId)
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
                        whenever(companyrepository.loadPublishersForGame(gameId)).thenReturn(Single.error(Throwable("foo")))
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
        }
    }
}