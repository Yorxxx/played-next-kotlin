package com.piticlistudio.playednext.domain.interactor.relation

import android.arch.persistence.room.EmptyResultSetException
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class LoadRelationsForGameUseCaseTest {

    @Nested
    @DisplayName("Given a LoadRelationsForGameUseCase instance")
    inner class Instance {

        private lateinit var usecase: LoadRelationsForGameUseCase
        val gamerelationRepository: GameRelationRepository = mock()

        @BeforeEach
        internal fun setUp() {
            reset(gamerelationRepository)
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
                whenever(gamerelationRepository.loadForGameAndPlatform(anyInt(), anyInt())).thenReturn(Flowable.just(gamerelation))
                observer = usecase.execute(game).test()
            }

            @Test
            @DisplayName("Then should request repository")
            fun shouldRequestRepository() {
                game.platforms.forEach {
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
                        it.size == game.platforms.size
                    }
                }
            }

            @Nested
            @DisplayName("and GameRelationRepository emits EmptyResultSetException")
            inner class EmptyMatches {

                private val error = EmptyResultSetException("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(gamerelationRepository.loadForGameAndPlatform(anyInt(), anyInt())).thenReturn(Flowable.error(error))
                    observer = usecase.execute(game).test()
                }

                @Test
                @DisplayName("Then should create a new empty relation for each platform")
                fun withError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoErrors()
                        assertValueCount(1)
                        assertValue {
                            it.size == game.platforms.size
                        }
                        values()[0].forEach {
                            assertEquals(game, it.game)
                            assertNotNull(it.platform)
                            assertEquals(GameRelationStatus.NONE, it.status)
                            assertNotEquals(0, it.createdAt)
                            assertNotEquals(0, it.updatedAt)
                            assertEquals(it.createdAt, it.updatedAt)
                        }
                    }
                }
            }

            @Nested
            @DisplayName("and GameRelationRepository emits other error")
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
                    }
                }
            }
        }
    }
}