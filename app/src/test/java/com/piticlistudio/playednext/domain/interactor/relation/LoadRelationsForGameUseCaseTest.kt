package com.piticlistudio.playednext.domain.interactor.relation

import android.arch.persistence.room.EmptyResultSetException
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.invocation.Invocation
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import kotlin.test.assertEquals

internal class LoadRelationsForGameUseCaseTest {

    @Nested
    @DisplayName("Given a LoadRelationsForGameUseCase instance")
    inner class Instance {

        private lateinit var usecase: LoadRelationsForGameUseCase
        @Mock private lateinit var gamerelationRepository: GameRelationRepository

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            usecase = LoadRelationsForGameUseCase(gamerelationRepository)
        }

        @Nested
        @DisplayName("When we call execute")
        inner class ExecuteIsCalled {

            private val game = makeGame()
            private var observer: TestSubscriber<List<GameRelation>>? = null
            private val gamerelation = makeGameRelation()

            @BeforeEach
            internal fun setUp() {
                val relFlow = Flowable.create<GameRelation>({
                    it.onNext(gamerelation)
                }, BackpressureStrategy.MISSING)
                whenever(gamerelationRepository.loadForGameAndPlatform(anyInt(), anyInt())).thenReturn(relFlow)
                observer = usecase.execute(game).test()
            }

            @Test
            @DisplayName("Then should request repository")
            fun shouldRequestRepository() {
                game.platforms?.forEach {
                    verify(gamerelationRepository).loadForGameAndPlatform(game.id, it.id)
                }
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertValueCount(1)
                    assertValue {
                        it.size == game.platforms?.size
                    }
                }
            }

            @Nested
            @DisplayName("and GameRelationRepository emits error")
            inner class RepositoryError {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(gamerelationRepository.loadForGameAndPlatform(anyInt(), anyInt())).thenReturn(Flowable.error(error))
                    observer = usecase.execute(game).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertError(error)
                        assertNoValues()
                        assertNotComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and GameRelationRepository emits EmptyResultSetException")
            inner class RepositoryEmptyResultSetException {

                private val error = EmptyResultSetException("foo")

                @BeforeEach
                internal fun setUp() {
                    val platforms = listOf<Platform>(makePlatform(10), makePlatform(20), makePlatform(30))
                    game.platforms = platforms
                    val relFlow = Flowable.create<GameRelation>({
                        it.onNext(gamerelation)
                    }, BackpressureStrategy.MISSING)
                    val errFlow = Flowable.create<GameRelation>({
                        it.onError(error)
                    }, BackpressureStrategy.MISSING)
                    reset(gamerelationRepository)
                    whenever(gamerelationRepository.loadForGameAndPlatform(anyInt(), anyInt())).thenAnswer(Answer {
                        val args = it.arguments
                        val platformId: Int = args[1] as Int
                        if (platformId == 20) {
                            errFlow
                        } else {
                            relFlow
                        }
                    })

                    observer = usecase.execute(game).test()
                }

                @Test
                @DisplayName("Then should emit values")
                fun withError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertValueCount(1)
                        assertValue {
                            it.size == 3
                        }
                        assertValue {  it.get(1).currentStatus == GameRelationStatus.NONE }
                        assertValue {
                            it.get(0) == gamerelation && it.get(2) == gamerelation
                        }
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }
        }
    }
}