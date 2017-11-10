package com.piticlistudio.playednext.domain.interactor.platform

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.domain.repository.PlatformRepository
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.*
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class LoadPlatformUseCaseTest {

    @Nested
    @DisplayName("Given LoadPlatformUseCase instance")
    inner class Instance {

        private lateinit var usecase: LoadPlatformUseCase
        @Mock
        private lateinit var platformRepository: PlatformRepository

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            usecase = LoadPlatformUseCase(platformRepository)
        }

        @Nested
        @DisplayName("When we call execute")
        inner class ExecuteIsCalled {

            private var testObserver: TestObserver<Platform>? = null
            private val platformId = 100
            private val result = makePlatform()

            @BeforeEach
            internal fun setUp() {
                whenever(platformRepository.load(anyInt())).thenReturn(Single.just(result))
                testObserver = usecase.execute(platformId).test()
            }

            @Test
            @DisplayName("Then should request repository")
            fun requestsRepository() {
                verify(platformRepository).load(platformId)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                Assertions.assertNotNull(testObserver)
                with(testObserver) {
                    this!!.assertNoErrors()
                    assertComplete()
                    assertValue(result)
                }
            }

            @Nested
            @DisplayName("And repository emits error")
            inner class ErrorEmitted {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(platformRepository.load(anyInt())).thenReturn(Single.error(error))
                    testObserver = usecase.execute(platformId).test()
                }

                @Test
                @DisplayName("Then emits errors")
                fun withoutErrors() {
                    Assertions.assertNotNull(testObserver)
                    testObserver?.apply {
                        assertNoValues()
                        assertNotComplete()
                        assertError(error)
                    }
                }
            }
        }
    }
}