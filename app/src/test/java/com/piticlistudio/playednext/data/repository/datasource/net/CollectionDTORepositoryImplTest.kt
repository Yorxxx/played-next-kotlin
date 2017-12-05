package com.piticlistudio.playednext.data.repository.datasource.net

import android.arch.persistence.room.EmptyResultSetException
import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.mapper.datasources.CollectionDTOMapper
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.test.factory.CollectionFactory
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollectionDTO
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameRemote
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class CollectionDTORepositoryImplTest {

    @Nested
    @DisplayName("Given a CollectionDTORepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: CollectionDTORepositoryImpl
        @Mock
        private lateinit var service: IGDBService
        @Mock
        private lateinit var mapper: CollectionDTOMapper

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = CollectionDTORepositoryImpl(service, mapper)
        }

        @Nested
        @DisplayName("When we call load")
        inner class LoadCalled {

            private var observer: TestObserver<Collection>? = null
            private val source = makeCollectionDTO()
            private val result = CollectionFactory.makeCollection()

            @BeforeEach
            internal fun setUp() {
                whenever(service.loadCollection(source.id)).thenReturn(Single.just(listOf(source)))
                whenever(mapper.mapFromModel(source)).thenReturn(result)
                observer = repository.load(source.id).test()
            }

            @Test
            @DisplayName("Then should request service")
            fun shouldRequestRepository() {
                verify(service).loadCollection(source.id)
            }

            @Test
            @DisplayName("Then should map result")
            fun shouldMap() {
                verify(mapper).mapFromModel(source)
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

            @Nested
            @DisplayName("And returns empty")
            inner class EmptyList {

                @BeforeEach
                internal fun setUp() {
                    whenever(service.loadCollection(source.id)).thenReturn(Single.just(listOf()))
                    reset(mapper)
                    observer = repository.load(source.id).test()
                }

                @Test
                @DisplayName("Then should not map")
                fun noMapping() {
                    verifyZeroInteractions(mapper)
                }

                @Test
                @DisplayName("Then emits error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertNotComplete()
                        assertError { it is EmptyResultSetException }
                    }
                }
            }

            @Nested
            @DisplayName("And mapping fails")
            inner class MappingFailure {

                @BeforeEach
                internal fun setUp() {
                    whenever(mapper.mapFromModel(any())).thenReturn(null)
                    observer = repository.load(source.id).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withoutErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertError(Throwable::class.java)
                        assertNoValues()
                        assertNotComplete()
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call save")
        inner class SaveCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makeCollection()

            @BeforeEach
            internal fun setUp() {
                observer = repository.save(source).test()
            }

            @Test
            @DisplayName("Then should emit notAllowedException")
            fun shouldRequestDao() {
                assertNotNull(observer)
                observer?.apply {
                    assertError(Throwable::class.java)
                }
            }
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class LoadForGameCalled {

            private var observer: TestObserver<Collection>? = null
            private val result = CollectionFactory.makeCollection()
            private val game = makeGameRemote()
            private val gameId = 10

            @BeforeEach
            internal fun setUp() {
                whenever(service.loadGame(gameId, "id,name,slug,url,created_at,updated_at,collection", "collection"))
                        .thenReturn(Single.just(listOf(game)))
                whenever(mapper.mapFromModel(game.collection)).thenReturn(result)
                observer = repository.loadForGame(gameId).test()
            }

            @Test
            @DisplayName("Then should request game")
            fun shouldRequestDao() {
                verify(service).loadGame(10, "id,name,slug,url,created_at,updated_at,collection", "collection")
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

            @Nested
            @DisplayName("And there is no collection")
            inner class WithoutCollection {

                @BeforeEach
                internal fun setUp() {
                    game.collection = null
                    whenever(service.loadGame(gameId, "id,name,slug,url,created_at,updated_at,collection", "collection"))
                            .thenReturn(Single.just(listOf(game.apply { collection = null })))
                    observer = repository.loadForGame(gameId).test()
                }

                @Test
                @DisplayName("Then should emit notFoundException")
                fun withoutErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertError(Throwable::class.java)
                        assertNoValues()
                        assertNotComplete()
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call saveForGame")
        inner class SaveForGameCalled {

            private var observer: TestObserver<Void>? = null
            private val source = CollectionFactory.makeCollection()
            private val gameId = 50

            @BeforeEach
            internal fun setUp() {
                observer = repository.saveForGame(gameId, source).test()
            }

            @Test
            @DisplayName("Then should emit notAllowedException")
            fun shouldRequestDao() {
                assertNotNull(observer)
                observer?.apply {
                    assertError(Throwable::class.java)
                }
            }
        }
    }
}