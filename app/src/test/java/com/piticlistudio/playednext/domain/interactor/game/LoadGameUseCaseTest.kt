package com.piticlistudio.playednext.domain.interactor.game

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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoadGameUseCaseTest {

    @Nested
    @DisplayName("Given a LoadGameUseCase instance")
    inner class LoadGameUseCaseInstance {

        @Mock lateinit var repository: GameRepository
        @Mock lateinit var companyrepository: CompanyRepository

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

            @BeforeEach
            internal fun setup() {
                Mockito.`when`(repository.load(10))
                        .thenReturn(Single.just(result))
                testObserver = useCase?.execute(10)!!.test()
            }

            @Test
            @DisplayName("Then requests repository")
            fun requestsRepository() {
                verify(repository).load(10)
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
            fun withoutDevls() {
                verifyZeroInteractions(companyrepository)
            }

            @Nested
            @DisplayName("And does not have developers assigned")
            inner class withoutDevelopers {

                val companyList = makeCompanyList()

                @BeforeEach
                internal fun setUp() {
                    result.developers = null
                    whenever(companyrepository.loadDevelopersForGameId(10)).thenReturn(Single.just(companyList))
                    whenever(repository.load(10)).thenReturn(Single.just(result))
                    testObserver = useCase?.execute(10)!!.test()
                }

                @Test
                @DisplayName("Then requests company repository")
                fun requestsRepository() {
                    verify(companyrepository).loadDevelopersForGameId(10)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun withoutErrors() {
                    assertNotNull(testObserver)
                    with(testObserver) {
                        this!!.assertNoErrors()
                        assertComplete()
                        assertValue(result)
                        assertNotNull(result.developers)
                        assertEquals(companyList, result.developers)
                    }
                }

                @Nested
                @DisplayName("And request fails")
                inner class requestFails {

                    @BeforeEach
                    internal fun setUp() {
                        result.developers = null
                        whenever(companyrepository.loadDevelopersForGameId(10)).thenReturn(Single.error(Throwable("foo")))
                        whenever(repository.load(10)).thenReturn(Single.just(result))
                        testObserver = useCase?.execute(10)!!.test()
                    }

                    @Test
                    @DisplayName("Then emits ignores errors")
                    fun withoutErrors() {
                        assertNotNull(testObserver)
                        with(testObserver) {
                            this!!.assertNoErrors()
                            assertComplete()
                            assertValue(result)
                            assertNull(result.developers)
                        }
                    }
                }
            }
        }
    }
}