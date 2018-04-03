package com.piticlistudio.playednext.data.repository.datasource.room.platform

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.PlatformDaoMapper
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatformDao
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class PlatformDaoRepositoryImplTest {

    @Nested
    @DisplayName("Given a PlatformDaoRepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: PlatformDaoRepositoryImpl
        @Mock
        private lateinit var dao: PlatformDaoService
        @Mock
        private lateinit var mapper: PlatformDaoMapper

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = PlatformDaoRepositoryImpl(dao, mapper)
        }

        @Nested
        @DisplayName("When we call load")
        inner class LoadCalled {

            private var observer: TestObserver<Platform>? = null
            private val source = makePlatformDao()
            private val result = makePlatform()
            private val id = 10L

            @BeforeEach
            internal fun setUp() {
                whenever(dao.find(id)).thenReturn(Single.just(source))
                whenever(mapper.mapFromModel(source)).thenReturn(result)
                observer = repository.load(id.toInt()).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestRepository() {
                verify(dao).find(id)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertValueCount(1)
                    assertValue(result)
                }
            }
        }

        @Nested
        @DisplayName("When we call save")
        inner class SaveCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makePlatform()
            private val result = makePlatformDao()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromEntity(source)).thenReturn(result)
                whenever(dao.insert(result)).thenReturn(randomLong())
                observer = repository.save(source).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).insert(result)
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
        @DisplayName("When we call loadForGame")
        inner class LoadForGameCalled {

            private var observer: TestObserver<List<Platform>>? = null
            private val source = randomListOf{ makePlatformDao() }
            private val result = randomListOf { makePlatform() }
            private val id = 10

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromModel(source)).thenReturn(result)
                whenever(dao.findForGame(id)).thenReturn(Single.just(source))
                observer = repository.loadForGame(id).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).findForGame(id)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertValue(result)
                }
            }
        }

        @Nested
        @DisplayName("When we call saveForGame")
        inner class SaveForGameCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makePlatform()
            private val platformDao = makePlatformDao()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromEntity(source)).thenReturn(platformDao)
                whenever(dao.insert(platformDao)).thenReturn(randomLong())
                whenever(dao.insertGamePlatform(any())).thenReturn(randomLong())
                observer = repository.saveForGame(10, source).test()
            }

            @Test
            @DisplayName("Then should save platform")
            fun shouldSaveCompany() {
                verify(dao).insert(platformDao)
            }

            @Test
            @DisplayName("Then should save relation")
            fun shouldRequestDao() {
                verify(dao).insertGamePlatform(com.nhaarman.mockito_kotlin.check {
                    kotlin.test.assertEquals(it.platformId, source.id)
                    kotlin.test.assertEquals(it.gameId, 10)
                })
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