package com.piticlistudio.playednext.domain.interactor.game

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.interactor.platform.LoadPlatformsForGameUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.GameRepository
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGameImage
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
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LoadGameUseCaseTest {

    @Nested
    @DisplayName("Given a LoadGameUseCase instance")
    inner class LoadGameUseCaseInstance {

        @Mock
        lateinit var repository: GameRepository
        @Mock
        lateinit var platformRepository: LoadPlatformsForGameUseCase

        private val gameId = 10
        private var useCase: LoadGameUseCase? = null

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            useCase = LoadGameUseCase(repository, platformRepository)
        }

        @Nested
        @DisplayName("When execute is called")
        inner class ExecuteIsCalled {

            private var testObserver: TestSubscriber<Game>? = null
            val result = makeGame()
            val collection = makeCollection()
            val platforms = randomListOf(5) { makePlatform() }
            val images = randomListOf(10) { makeGameImage() }

            @BeforeEach
            internal fun setup() {
                val flowable = Flowable.create<Game>({ it.onNext(result) }, BackpressureStrategy.MISSING)
                whenever(repository.load(gameId)).thenReturn(flowable)
                whenever(platformRepository.execute(result)).thenReturn(Single.just(platforms))
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
        }
    }
}