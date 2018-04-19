package com.piticlistudio.playednext.data.repository.datasource.room.company

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.mapper.datasources.company.RoomCompanyMapper
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.factory.CompanyFactory
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RoomCompanyRepositoryImplTest {

    @Nested
    @DisplayName("Given a RoomCompanyRepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: RoomCompanyRepositoryImpl
        private val dao: RoomCompanyService = mock()
        private val companyMapper: RoomCompanyMapper = mock()

        @BeforeEach
        internal fun setUp() {
            reset(dao, companyMapper)
            repository = RoomCompanyRepositoryImpl(dao, companyMapper)
        }

        @Nested
        @DisplayName("When we call save")
        inner class SaveCalled {

            private var observer: TestObserver<Void>? = null
            private val source = CompanyFactory.makeCompany()
            private val result = CompanyFactory.makeCompanyRoom()

            @BeforeEach
            internal fun setUp() {
                whenever(companyMapper.mapIntoDataLayerModel(source)).thenReturn(result)
                whenever(dao.insert(result)).thenReturn(10)
                observer = repository.save(source).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).insert(result)
            }

            @Test
            @DisplayName("Then should map response")
            fun shouldMap() {
                verify(companyMapper).mapIntoDataLayerModel(source)
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
            @DisplayName("And Room fails to save")
            inner class RoomFail {

                @BeforeEach
                internal fun setUp() {
                    reset(dao, companyMapper)
                    whenever(dao.insert(result)).thenReturn(-1)

                    observer = repository.save(source).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertError(CompanySaveException::class.java)
                        assertNotComplete()
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call loadDevelopersForGame")
        inner class LoadDevelopersForGameCalled {

            private var observer: TestSubscriber<List<Company>>? = null
            private val daoModel = CompanyFactory.makeCompanyRoom()
            private val domainModel = CompanyFactory.makeCompany()
            private val source = listOf(daoModel)

            @BeforeEach
            internal fun setUp() {
                whenever(companyMapper.mapFromDataLayer(daoModel)).thenReturn(domainModel)
                whenever(dao.findDeveloperForGame(10)).thenReturn(Flowable.just(source))
                observer = repository.loadDevelopersForGame(10).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).findDeveloperForGame(10)
            }

            @Test
            @DisplayName("Then should map response")
            fun shouldMap() {
                verify(companyMapper).mapFromDataLayer(daoModel)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertValue {
                        it.size == source.size
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call loadPublishersForGame")
        inner class LoadPublishersForGameCalled {

            private var observer: TestSubscriber<List<Company>>? = null
            private val daoModel = CompanyFactory.makeCompanyRoom()
            private val domainModel = CompanyFactory.makeCompany()
            private val source = listOf(daoModel)

            @BeforeEach
            internal fun setUp() {
                whenever(companyMapper.mapFromDataLayer(daoModel)).thenReturn(domainModel)
                whenever(dao.findPublishersForGame(10)).thenReturn(Flowable.just(source))
                observer = repository.loadPublishersForGame(10).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).findPublishersForGame(10)
            }

            @Test
            @DisplayName("Then should map response")
            fun shouldMap() {
                verify(companyMapper).mapFromDataLayer(daoModel)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertValue {
                        it.size == source.size
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call saveDeveloperForGame")
        inner class SaveDeveloperForGameCalled {

            private var observer: TestObserver<Void>? = null
            private val source = CompanyFactory.makeCompany()
            private val companyDao = CompanyFactory.makeCompanyRoom()

            @BeforeEach
            internal fun setUp() {
                whenever(companyMapper.mapIntoDataLayerModel(source)).thenReturn(companyDao)
                whenever(dao.insert(companyDao)).thenReturn(10)
                whenever(dao.insertGameDeveloper(any())).thenReturn(10)
                observer = repository.saveDeveloperForGame(10, source).test()
            }

            @Test
            @DisplayName("Then should save company")
            fun shouldSaveCompany() {
                verify(dao).insert(companyDao)
            }

            @Test
            @DisplayName("Then should save publisher relation")
            fun shouldRequestDao() {
                verify(dao).insertGameDeveloper(com.nhaarman.mockito_kotlin.check {
                    assertEquals(it.companyId, source.id)
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
            @DisplayName("And Room fails to save company")
            inner class RoomFail {

                @BeforeEach
                internal fun setUp() {
                    reset(dao, companyMapper)
                    whenever(dao.insert(companyDao)).thenReturn(-1)

                    observer = repository.saveDeveloperForGame(10, source).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertError(CompanySaveException::class.java)
                        assertNotComplete()
                    }
                }

                @Test
                @DisplayName("Then should not save RoomGameDeveloper")
                fun shouldNotSaveGameDeveloper() {
                    verify(dao, never()).insertGameDeveloper(any())
                }
            }

            @Nested
            @DisplayName("And Room fails to save game developer")
            inner class RoomPublisherFail {

                @BeforeEach
                internal fun setUp() {
                    reset(dao)
                    whenever(dao.insert(companyDao)).thenReturn(10)
                    whenever(dao.insertGameDeveloper(any())).thenReturn(-1)

                    observer = repository.saveDeveloperForGame(10, source).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertError(GameDeveloperSaveException::class.java)
                        assertNotComplete()
                    }
                }

                @Test
                @DisplayName("Then should have saved company")
                fun shouldHaveSavedCompany() {
                    verify(dao).insert(companyDao)
                }
            }
        }

        @Nested
        @DisplayName("When we call savePublisherForGame")
        inner class SavePublisherForGameCalled {

            private var observer: TestObserver<Void>? = null
            private val source = CompanyFactory.makeCompany()
            private val companyDao = CompanyFactory.makeCompanyRoom()

            @BeforeEach
            internal fun setUp() {
                whenever(companyMapper.mapIntoDataLayerModel(source)).thenReturn(companyDao)
                whenever(dao.insert(companyDao)).thenReturn(10)
                whenever(dao.insertGamePublisher(any())).thenReturn(10)
                observer = repository.savePublisherForGame(10, source).test()
            }

            @Test
            @DisplayName("Then should save company")
            fun shouldSaveCompany() {
                verify(dao).insert(companyDao)
            }

            @Test
            @DisplayName("Then should save publisher relation")
            fun shouldRequestDao() {
                verify(dao).insertGamePublisher(com.nhaarman.mockito_kotlin.check {
                    assertEquals(it.companyId, source.id)
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
            @DisplayName("And Room fails to save company")
            inner class RoomFail {

                @BeforeEach
                internal fun setUp() {
                    reset(dao, companyMapper)
                    whenever(dao.insert(companyDao)).thenReturn(-1)

                    observer = repository.savePublisherForGame(10, source).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertError(Throwable::class.java)
                        assertNotComplete()
                    }
                }

                @Test
                @DisplayName("Then should not save RoomGamePublisher")
                fun shouldNotSaveGameDeveloper() {
                    verify(dao, never()).insertGamePublisher(any())
                }
            }

            @Nested
            @DisplayName("And Room fails to save GamePublisher")
            inner class RoomPublisherFail {

                @BeforeEach
                internal fun setUp() {
                    reset(dao)
                    whenever(dao.insert(companyDao)).thenReturn(10)
                    whenever(dao.insertGamePublisher(any())).thenReturn(-1)

                    observer = repository.savePublisherForGame(10, source).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertError(GamePublisherSaveException::class.java)
                        assertNotComplete()
                    }
                }

                @Test
                @DisplayName("Then should have saved company")
                fun shouldHaveSavedCompany() {
                    verify(dao).insert(companyDao)
                }
            }
        }
    }
}