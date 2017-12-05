package com.piticlistudio.playednext.data.repository

import android.arch.persistence.room.EmptyResultSetException
import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.repository.datasource.dao.CompanyDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.net.CompanyRemoteImpl
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompany
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
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
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertTrue

internal class CompanyRepositoryImplTest {

    @Nested
    @DisplayName("Given CompanyRepositoryImpl instance")
    inner class Instance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock
        private lateinit var localImpl: CompanyDaoRepositoryImpl
        @Mock
        private lateinit var remoteImpl: CompanyRemoteImpl

        private var repository: CompanyRepositoryImpl? = null

        @BeforeEach
        fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = CompanyRepositoryImpl(localImpl, remoteImpl)
        }

        @Nested
        @DisplayName("When we call load")
        inner class Load {
            val id = 10
            val entity = makeCompany()
            var result: TestObserver<Company>? = null

            @BeforeEach
            fun setup() {
                whenever(localImpl.load(id)).thenReturn(Single.just(entity))
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
            inner class WithoutLocalResult {

                @BeforeEach
                internal fun setUp() {
                    whenever(localImpl.load(id)).thenReturn(Single.error(EmptyResultSetException("no results")))
                    whenever(remoteImpl.load(id)).thenReturn(Single.just(entity))
                    whenever(localImpl.save(entity)).thenReturn(Completable.complete())
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

        @Nested
        @DisplayName("When we call loadDevelopersForGameId")
        inner class LoadDevelopersForGameIdCalled {
            val id = 10
            val entity = randomListOf(5) { makeCompany() }
            var result: TestObserver<List<Company>>? = null

            @BeforeEach
            fun setup() {
                whenever(localImpl.loadDevelopersForGame(id)).thenReturn(Single.just(entity))
                result = repository?.loadDevelopersForGameId(id)?.test()
            }

            @Test
            @DisplayName("Then should request local repository")
            fun localIsCalled() {
                verify(localImpl).loadDevelopersForGame(id)
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
                    whenever(localImpl.loadDevelopersForGame(id)).thenReturn(Single.just(listOf()))
                    whenever(remoteImpl.loadDevelopersForGame(id)).thenReturn(Single.just(entity))
                    whenever(localImpl.saveDeveloperForGame(anyInt(), any())).thenReturn(Completable.complete())
                    result = repository?.loadDevelopersForGameId(id)?.test()
                }

                @Test
                @DisplayName("Then should request remote repository")
                fun remoteIsCalled() {
                    verify(remoteImpl).loadDevelopersForGame(id)
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
                    verify(localImpl, times(entity.size)).saveDeveloperForGame(anyInt(), any())
                }
            }
        }

        @Nested
        @DisplayName("When we call saveDevelopersForGame")
        inner class SaveDevelopersForGameCalled {

            private val companies = randomListOf(10) { makeCompany() }
            var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(localImpl.saveDeveloperForGame(anyInt(), any())).thenReturn(Completable.complete())
                observer = repository?.saveDevelopersForGame(10, companies)?.test()
            }

            @Test
            @DisplayName("Then should request local repository")
            fun savesInLocalRepository() {
                verify(localImpl, times(companies.size)).saveDeveloperForGame(anyInt(), check {
                    assertTrue(companies.contains(it))
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
        @DisplayName("When we call loadPublishersForGame")
        inner class LoadPublishersForGameCalled {
            val id = 10
            val entity = randomListOf(20) { makeCompany() }
            var result: TestObserver<List<Company>>? = null

            @BeforeEach
            fun setup() {
                whenever(localImpl.loadPublishersForGame(id)).thenReturn(Single.just(entity))
                result = repository?.loadPublishersForGame(id)?.test()
            }

            @Test
            @DisplayName("Then should request local repository")
            fun localIsCalled() {
                verify(localImpl).loadPublishersForGame(id)
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
                    whenever(localImpl.loadPublishersForGame(id)).thenReturn(Single.just(listOf()))
                    whenever(remoteImpl.loadPublishersForGame(id)).thenReturn(Single.just(entity))
                    whenever(localImpl.savePublisherForGame(anyInt(), any())).thenReturn(Completable.complete())
                    result = repository?.loadPublishersForGame(id)?.test()
                }

                @Test
                @DisplayName("Then should request remote repository")
                fun remoteIsCalled() {
                    verify(remoteImpl).loadPublishersForGame(id)
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
                    verify(localImpl, times(entity.size)).savePublisherForGame(anyInt(), any())
                }
            }
        }

        @Nested
        @DisplayName("When we call savePublishersForGame")
        inner class SavePublishersForGameCalled {

            private val companies = randomListOf(10) { makeCompany() }
            var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(localImpl.savePublisherForGame(anyInt(), any())).thenReturn(Completable.complete())
                observer = repository?.savePublishersForGame(10, companies)?.test()
            }

            @Test
            @DisplayName("Then should request local repository")
            fun savesInLocalRepository() {
                verify(localImpl, times(companies.size)).savePublisherForGame(anyInt(), check {
                    assertTrue(companies.contains(it))
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
    }
}