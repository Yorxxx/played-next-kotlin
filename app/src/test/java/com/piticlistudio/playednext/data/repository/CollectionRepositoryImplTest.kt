package com.piticlistudio.playednext.data.repository

import android.arch.persistence.room.EmptyResultSetException
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.repository.datasource.dao.CollectionDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.net.CollectionDTORepositoryImpl
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class CollectionRepositoryImplTest {

    @Nested
    @DisplayName("Given CollectionRepositoryImpl instance")
    inner class Instance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock
        private lateinit var localImpl: CollectionDaoRepositoryImpl
        @Mock
        private lateinit var remoteImpl: CollectionDTORepositoryImpl

        private var repository: CollectionRepositoryImpl? = null

        @BeforeEach
        fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = CollectionRepositoryImpl(localImpl, remoteImpl)
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class LoadForGameCalled {
            val id = 10
            val entity = makeCollection()
            var result: TestObserver<Collection>? = null

            @BeforeEach
            fun setup() {
                whenever(localImpl.loadForGame(id)).thenReturn(Single.just(entity))
                result = repository?.loadForGame(id)?.test()
            }

            @Test
            @DisplayName("Then should request local repository")
            fun localIsCalled() {
                verify(localImpl).loadForGame(id)
            }

            @Test
            @DisplayName("Then should not request remote repository")
            fun remoteIsNotCalled() {
                verifyZeroInteractions(remoteImpl)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(result)
                result?.apply {
                    assertNoErrors()
                    assertValueCount(1)
                    assertComplete()
                    assertValue(entity)
                }
            }

            @Nested
            @DisplayName("And there is no result in local repository")
            inner class WithoutLocalResult {

                @BeforeEach
                internal fun setUp() {
                    whenever(localImpl.loadForGame(id)).thenReturn(Single.error(EmptyResultSetException("foo")))
                    whenever(remoteImpl.loadForGame(id)).thenReturn(Single.just(entity))
                    whenever(localImpl.saveForGame(ArgumentMatchers.anyInt(), any())).thenReturn(Completable.complete())
                    result = repository?.loadForGame(id)?.test()
                }

                @Test
                @DisplayName("Then should request remote repository")
                fun remoteIsCalled() {
                    verify(remoteImpl).loadForGame(id)
                }

                @Test
                @DisplayName("Then should emit without errors")
                fun withoutErrors() {
                    assertNotNull(result)
                    with(result) {
                        this?.assertNoErrors()
                        this?.assertValueCount(1)
                        this?.assertComplete()
                        this?.assertValue(entity)
                    }
                }

                @Test
                @DisplayName("Then should cache retrieved data")
                fun cacheResponse() {
                    verify(localImpl).saveForGame(id, entity)
                }
            }
        }
    }
}