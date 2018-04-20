package com.piticlistudio.playednext.domain.interactor.relation

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.factory.GameRelationFactory.Factory.makeGameRelation
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class LoadRelationsWithStatusUseCaseTest {

    @Nested
    @DisplayName("Given LoadRelationsWithStatusUseCase instance")
    inner class Instance {

        private lateinit var useCase: LoadRelationsWithStatusUseCase
        private val repository: GameRelationRepository = mock()

        @BeforeEach
        internal fun setUp() {
            reset(repository)
            useCase = LoadRelationsWithStatusUseCase(repository)
        }

        @Nested
        @DisplayName("When we call execute")
        inner class ExecuteCalled {

            private var observer: TestSubscriber<List<GameRelation>>? = null
            private val data = randomListOf(10) { makeGameRelation() }
            private val status = GameRelationStatus.COMPLETED

            @BeforeEach
            internal fun setUp() {
                whenever(repository.loadWithStatus(any())).thenReturn(Flowable.just(data))
                observer = useCase.execute(status).test()
            }

            @Test
            @DisplayName("Then should request repository")
            fun requestsRepository() {
                verify(repository).loadWithStatus(status)
                verify(repository, never()).loadWithStatus(GameRelationStatus.BEATEN)
                verify(repository, never()).loadWithStatus(GameRelationStatus.PLAYED)
                verify(repository, never()).loadWithStatus(GameRelationStatus.UNPLAYED)
                verify(repository, never()).loadWithStatus(GameRelationStatus.PLAYING)
                verify(repository, never()).loadWithStatus(GameRelationStatus.NONE)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertValueCount(1)
                    assertValue(data)
                }
            }

        }
    }
}