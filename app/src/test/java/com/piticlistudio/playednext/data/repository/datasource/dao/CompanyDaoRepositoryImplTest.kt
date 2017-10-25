package com.piticlistudio.playednext.data.repository.datasource.dao

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyDaoMapper
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompany
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyDao
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyDaoList
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyList
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

internal class CompanyDaoRepositoryImplTest {

    @Nested
    @DisplayName("Given a CompanyDaoRepositoryImpl instance")
    inner class instance {

        private lateinit var repository: CompanyDaoRepositoryImpl
        @Mock
        private lateinit var dao: CompanyDaoService
        @Mock
        private lateinit var mapper: CompanyDaoMapper

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = CompanyDaoRepositoryImpl(dao, mapper)
        }

        @Nested
        @DisplayName("When we call load")
        inner class loadCalled {

            private var observer: TestObserver<Company>? = null
            private val source = makeCompanyDao()
            private val result = makeCompany()

            @BeforeEach
            internal fun setUp() {
                whenever(dao.findCompanyById(10)).thenReturn(Single.just(source))
                whenever(mapper.mapFromModel(source)).thenReturn(result)
                observer = repository.load(10).test();
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestRepository() {
                verify(dao).findCompanyById(10)
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
        inner class saveCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makeCompany()
            private val result = makeCompanyDao()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromEntity(source)).thenReturn(result)
                whenever(dao.insertCompany(result)).thenReturn(10)
                observer = repository.save(source).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).insertCompany(result)
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
        @DisplayName("When we call loadDevelopersForGame")
        inner class loadDevelopersForGameCalled {

            private var observer: TestObserver<List<Company>>? = null
            private val source = makeCompanyDaoList()
            private val result = makeCompanyList()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromModel(source)).thenReturn(result)
                whenever(dao.findDeveloperForGame(10)).thenReturn(Single.just(source))
                observer = repository.loadDevelopersForGame(10).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).findDeveloperForGame(10)
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
        @DisplayName("When we call loadPublishersForGame")
        inner class loadPublishersForGameCalled {

            private var observer: TestObserver<List<Company>>? = null
            private val source = makeCompanyDaoList()
            private val result = makeCompanyList()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromModel(source)).thenReturn(result)
                whenever(dao.findPublishersForGame(10)).thenReturn(Single.just(source))
                observer = repository.loadPublishersForGame(10).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).findPublishersForGame(10)
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
        @DisplayName("When we call saveDeveloperForGame")
        inner class saveDeveloperForGameCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makeCompany()
            private val companyDao = makeCompanyDao()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromEntity(source)).thenReturn(companyDao)
                whenever(dao.insertCompany(companyDao)).thenReturn(10)
                whenever(dao.insertGameDeveloper(any())).thenReturn(10)
                observer = repository.saveDeveloperForGame(10, source).test()
            }

            @Test
            @DisplayName("Then should save company")
            fun shouldSaveCompany() {
                verify(dao).insertCompany(companyDao)
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
        }

        @Nested
        @DisplayName("When we call savePublisherForGame")
        inner class savePublisherForGameCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makeCompany()
            private val companyDao = makeCompanyDao()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromEntity(source)).thenReturn(companyDao)
                whenever(dao.insertCompany(companyDao)).thenReturn(10)
                whenever(dao.insertGamePublisher(any())).thenReturn(10)
                observer = repository.savePublisherForGame(10, source).test()
            }

            @Test
            @DisplayName("Then should save company")
            fun shouldSaveCompany() {
                verify(dao).insertCompany(companyDao)
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
        }
    }
}