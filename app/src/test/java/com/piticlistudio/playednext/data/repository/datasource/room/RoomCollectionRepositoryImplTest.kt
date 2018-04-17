package com.piticlistudio.playednext.data.repository.datasource.room

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.RoomCollectionMapper
import com.piticlistudio.playednext.data.repository.datasource.room.franchise.CollectionSaveException
import com.piticlistudio.playednext.data.repository.datasource.room.franchise.GameCollectionSaveException
import com.piticlistudio.playednext.data.repository.datasource.room.franchise.RoomCollectionRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.franchise.RoomCollectionService
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeRoomCollection
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNotNull

internal class RoomCollectionRepositoryImplTest {

    @Nested
    @DisplayName("Given a RoomCollectionRepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: RoomCollectionRepositoryImpl
        private val dao: RoomCollectionService = mock()
        private val mapper: RoomCollectionMapper = mock()

        @BeforeEach
        internal fun setUp() {
            reset(dao, mapper)
            repository = RoomCollectionRepositoryImpl(dao, mapper)
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class LoadCalled {

            private var observer: TestSubscriber<List<Collection>>? = null
            private val source = listOf(makeRoomCollection())
            private val result = makeCollection()

            @BeforeEach
            internal fun setUp() {
                whenever(dao.findForGame(10)).thenReturn(Flowable.just(source))
                whenever(mapper.mapFromDataLayer(source.first())).thenReturn(result)
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
                verify(mapper).mapFromDataLayer(source.first())
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertValueCount(1)
                    assertValue {
                        !it.isEmpty() && it.contains(result)
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call saveForGame")
        inner class SaveForGameCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makeCollection()
            private val result = makeRoomCollection()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapIntoDataLayerModel(source)).thenReturn(result)
                whenever(dao.insert(result)).thenReturn(10)
                whenever(dao.insertGameCollection(any())).thenReturn(10)
                observer = repository.saveForGame(10, source).test()
            }

            @Test
            @DisplayName("Then should save collection")
            fun shouldSaveCompany() {
                verify(dao).insert(result)
            }

            @Test
            @DisplayName("Then should save relation")
            fun shouldRequestDao() {
                verify(dao).insertGameCollection(com.nhaarman.mockito_kotlin.check {
                    Assertions.assertEquals(it.collectionId, source.id)
                    Assertions.assertEquals(it.gameId, 10)
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
            @DisplayName("And Room fails to save collection")
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
                        assertError(CollectionSaveException::class.java)
                        assertNotComplete()
                    }
                }

                @Test
                @DisplayName("Then should not save RoomGameCollection")
                fun shouldNotSaveGameDeveloper() {
                    verify(dao, never()).insertGameCollection(any())
                }
            }

            @Nested
            @DisplayName("And Room fails to save gameCollection")
            inner class RoomPublisherFail {

                @BeforeEach
                internal fun setUp() {
                    reset(dao)
                    whenever(dao.insert(result)).thenReturn(10)
                    whenever(dao.insertGameCollection(any())).thenReturn(-1)

                    observer = repository.saveForGame(10, source).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertError(GameCollectionSaveException::class.java)
                        assertNotComplete()
                    }
                }

                @Test
                @DisplayName("Then should have saved collection")
                fun shouldHaveSavedCompany() {
                    verify(dao).insert(result)
                }
            }
        }
    }
}