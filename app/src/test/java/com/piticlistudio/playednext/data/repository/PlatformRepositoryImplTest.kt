package com.piticlistudio.playednext.data.repository

import android.arch.persistence.room.EmptyResultSetException
import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.repository.datasource.dao.platform.PlatformDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.net.platform.PlatformDTORepositoryImpl
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatformList
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
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class PlatformRepositoryImplTest {

    @Nested
    @DisplayName("Given PlatformRepositoryImpl instance")
    inner class instance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock
        private lateinit var localImpl: PlatformDaoRepositoryImpl
        @Mock
        private lateinit var remoteImpl: PlatformDTORepositoryImpl

        private var repository: PlatformRepositoryImpl? = null

        @BeforeEach
        fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = PlatformRepositoryImpl(localImpl, remoteImpl)
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class loadForGameCalled {
            val id = 10
            val entity = makePlatformList()
            var result: TestObserver<List<Platform>>? = null

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
            inner class withoutLocalResult {

                @BeforeEach
                internal fun setUp() {
                    whenever(localImpl.loadForGame(id)).thenReturn(Single.just(listOf()))
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
                    verify(localImpl, times(entity.size)).saveForGame(ArgumentMatchers.anyInt(), any())
                }
            }
        }

        @Nested
        @DisplayName("When we call saveForGame")
        inner class saveForGameCalled {

            val data = makePlatformList()
            var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(localImpl.saveForGame(ArgumentMatchers.anyInt(), any())).thenReturn(Completable.complete())
                observer = repository?.saveForGame(10, data)?.test()
            }

            @Test
            @DisplayName("Then should request local repository")
            fun savesInLocalRepository() {
                verify(localImpl, times(data.size)).saveForGame(ArgumentMatchers.anyInt(), check {
                    kotlin.test.assertTrue(data.contains(it))
                })
            }

            @Test
            @DisplayName("Then should not request remote repository")
            fun remoteIsNotCalled() {
                verifyZeroInteractions(remoteImpl)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertNoValues()
                }
            }
        }

        @Nested
        @DisplayName("When we call load")
        inner class loadCalled {
            val id = 10
            val entity = makePlatform()
            var result: TestObserver<Platform>? = null

            @BeforeEach
            fun setup() {
                whenever(localImpl.load(anyInt())).thenReturn(Single.just(entity))
                result = repository?.load(id)?.test()
            }

            @Test
            @DisplayName("Then should request local repository")
            fun localIsCalled() {
                verify(localImpl).load(id)
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
            inner class withoutLocalResult {

                @BeforeEach
                internal fun setUp() {
                    whenever(localImpl.load(anyInt())).thenReturn(Single.error(EmptyResultSetException("no results")))
                    whenever(remoteImpl.load(id)).thenReturn(Single.just(entity))
                    whenever(localImpl.save(any())).thenReturn(Completable.complete())
                    result = repository?.load(id)?.test()
                }

                @Test
                @DisplayName("Then should request remote repository")
                fun remoteIsCalled() {
                    verify(remoteImpl).load(id)
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
                    verify(localImpl).save(entity)
                }
            }
        }
    }
}