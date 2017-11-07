package com.piticlistudio.playednext.domain.interactor.relation

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class SaveGameRelationUseCaseTest {

    @Nested
    @DisplayName("Given a SaveGameRelationUseCase")
    inner class Instance {

        private lateinit var usecase: SaveGameRelationUseCase
        @Mock private lateinit var repository: GameRelationRepository

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            usecase = SaveGameRelationUseCase(repository)
        }

        @Nested
        @DisplayName("When we call execute")
        inner class ExecuteIsCalled {

            private var observer: TestObserver<Void>? = null
            private val relation = makeGameRelation()

            @BeforeEach
            internal fun setUp() {
                whenever(repository.save(any())).thenReturn(Completable.complete())
                observer = usecase.execute(relation).test()
            }

            @Test
            @DisplayName("Then should request repository")
            fun callsRepository() {
                verify(repository).save(relation)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertNoValues()
                    assertComplete()
                }
            }
        }
    }
}