package com.piticlistudio.playednext.data.repository.datasource.room.platform

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.RoomPlatformMapper
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makeRoomPlatform
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RoomGamePlatformRepositoryImplTest {

    @Nested
    @DisplayName("Given a RoomGamePlatformRepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: RoomGamePlatformRepositoryImpl
        private val dao: RoomGamePlatformService = mock()
        private val mapper: RoomPlatformMapper = mock()

        @BeforeEach
        internal fun setUp() {
            reset(dao, mapper)
            repository = RoomGamePlatformRepositoryImpl(dao, mapper)
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class LoadCalled {

            private var observer: TestObserver<List<Platform>>? = null
            private val source = makeRoomPlatform()
            private val result = makePlatform()

            @BeforeEach
            internal fun setUp() {
                whenever(dao.findForGame(10)).thenReturn(Single.just(listOf(source)))
                whenever(mapper.mapFromDataLayer(source)).thenReturn(result)
                observer = repository.loadForGame(10).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestRepository() {
                verify(dao).findForGame(10)
            }

            @Test
            @DisplayName("Then should map response")
            fun shouldMap() {
                verify(mapper).mapFromDataLayer(source)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertValueCount(1)
                    assertValue { it.size == 1 && it.contains(result) }
                }
            }
        }

        @Nested
        @DisplayName("When we call saveForGame")
        inner class SaveForGameCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makePlatform()
            private val result = makeRoomPlatform()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapIntoDataLayerModel(source)).thenReturn(result)
                whenever(dao.insert(result)).thenReturn(10)
                whenever(dao.insertGamePlatform(any())).thenReturn(10)
                observer = repository.saveForGame(10, source).test()
            }

            @Test
            @DisplayName("Then should save company")
            fun shouldSaveCompany() {
                verify(dao).insert(result)
            }

            @Test
            @DisplayName("Then should save relation")
            fun shouldRequestDao() {
                verify(dao).insertGamePlatform(com.nhaarman.mockito_kotlin.check {
                    assertEquals(it.platformId, source.id)
                    assertEquals(it.gameId, 10)
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

            @Nested
            @DisplayName("And Room fails to save platform")
            inner class RoomFail {

                @BeforeEach
                internal fun setUp() {
                    reset(dao, mapper)
                    whenever(dao.insert(result)).thenReturn(-1)

                    observer = repository.saveForGame(10, source).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertError(PlatformSaveException::class.java)
                        assertNotComplete()
                    }
                }

                @Test
                @DisplayName("Then should not save RoomGameGenre")
                fun shouldNotSaveGameDeveloper() {
                    verify(dao, never()).insertGamePlatform(any())
                }
            }

            @Nested
            @DisplayName("And Room fails to save gamePlatform")
            inner class RoomPlatformFail {

                @BeforeEach
                internal fun setUp() {
                    reset(dao)
                    whenever(dao.insert(result)).thenReturn(10)
                    whenever(dao.insertGamePlatform(any())).thenReturn(-1)

                    observer = repository.saveForGame(10, source).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertError(GamePlatformSaveException::class.java)
                        assertNotComplete()
                    }
                }

                @Test
                @DisplayName("Then should have saved platform")
                fun shouldHaveSavedCompany() {
                    verify(dao).insert(result)
                }
            }
        }
    }
}