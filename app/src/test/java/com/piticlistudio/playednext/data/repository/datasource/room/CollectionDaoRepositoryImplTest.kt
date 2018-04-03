package com.piticlistudio.playednext.data.repository.datasource.room

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.CollectionDaoMapper
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollectionDao
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomLong
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class CollectionDaoRepositoryImplTest {

    @Nested
    @DisplayName("Given a CollectionDaoRepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: CollectionDaoRepositoryImpl
        @Mock
        private lateinit var dao: CollectionDaoService
        @Mock
        private lateinit var mapper: CollectionDaoMapper

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = CollectionDaoRepositoryImpl(dao, mapper)
        }

        @Nested
        @DisplayName("When we call load")
        inner class LoadCalled {

            private var observer: TestObserver<Collection>? = null
            private val source = makeCollectionDao()
            private val result = makeCollection()

            @BeforeEach
            internal fun setUp() {
                whenever(dao.find(source.id.toLong())).thenReturn(Single.just(source))
                whenever(mapper.mapFromModel(source)).thenReturn(result)
                observer = repository.load(source.id).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestRepository() {
                verify(dao).find(source.id.toLong())
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
            private val source = makeCollection()
            private val result = makeCollectionDao()

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

            @Nested
            @DisplayName("And map fails")
            inner class MappingFails {

                @BeforeEach
                internal fun setUp() {
                    whenever(mapper.mapFromEntity(source)).thenReturn(null)
                    whenever(dao.insert(result)).thenReturn(randomLong())
                    observer = repository.save(source).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withoutErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertError(Throwable::class.java)
                        assertNotComplete()
                        assertNoValues()
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class LoadForGameCalled {

            private var observer: TestObserver<Collection>? = null
            private val source = makeCollectionDao()
            private val result = makeCollection()
            private val gameId = 10

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromModel(source)).thenReturn(result)
                whenever(dao.findForGame(gameId)).thenReturn(Single.just(source))
                observer = repository.loadForGame(gameId).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).findForGame(gameId)
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
            private val source = makeCollection()
            private val result = makeCollectionDao()
            private val gameId = 50

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromEntity(source)).thenReturn(result)
                whenever(dao.insert(result)).thenReturn(randomLong())
                whenever(dao.insertGameCollection(any())).thenReturn(randomLong())
                observer = repository.saveForGame(gameId, source).test()
            }

            @Test
            @DisplayName("Then should save data")
            fun shouldSaveCompany() {
                verify(dao).insert(result)
            }

            @Test
            @DisplayName("Then should save relation")
            fun shouldRequestDao() {
                verify(dao).insertGameCollection(com.nhaarman.mockito_kotlin.check {
                    kotlin.test.assertEquals(it.collectionId, source.id)
                    kotlin.test.assertEquals(it.gameId, gameId)
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