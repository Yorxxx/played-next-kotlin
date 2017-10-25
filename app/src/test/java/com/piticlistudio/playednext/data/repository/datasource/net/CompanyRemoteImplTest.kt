package com.piticlistudio.playednext.data.repository.datasource.net

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyDTOMapper
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompany
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyDTO
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyList
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameRemote
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class CompanyRemoteImplTest() {

    @Nested
    @DisplayName("Given CompanyRemoteImpl instance")
    inner class CompanyRemoteImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock lateinit var service: IGDBService
        @Mock lateinit var mapper: CompanyDTOMapper

        private var repositoryImpl: CompanyRemoteImpl? = null

        @BeforeEach
        fun setup() {
            MockitoAnnotations.initMocks(this)
            repositoryImpl = CompanyRemoteImpl(service, mapper)
        }

        @Nested
        @DisplayName("When we call load")
        inner class Load {

            var result: TestObserver<Company>? = null
            val source = makeCompanyDTO()
            val entity = makeCompany()

            @BeforeEach
            fun setup() {
                whenever(service.loadCompany(10, "*"))
                        .thenReturn(Single.just(source))
                whenever(mapper.mapFromModel(source)).thenReturn(entity)
                result = repositoryImpl?.load(10)?.test()
            }

            @Test
            @DisplayName("Then should request service")
            fun serviceIsCalled() {
                verify(service).loadCompany(10, "*")
            }

            @Test
            @DisplayName("Then should map service response")
            fun mapIsCalled() {
                verify(mapper).mapFromModel(source)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(result)
                with(result) {
                    this?.assertValueCount(1)
                    this?.assertComplete()
                    this?.assertNoErrors()
                    this?.assertValue(entity)
                }
            }
        }

        @Nested
        @DisplayName("When we call save")
        inner class Save {

            val entity1 = makeCompany()
            var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                observer = repositoryImpl?.save(entity1)?.test()
            }

            @Test
            @DisplayName("Then should emit notAllowedException")
            fun throwsError() {
                assertNotNull(observer)
                with(observer) {
                    this!!.assertNotComplete()
                    assertNoValues()
                    assertError(Throwable::class.java)
                }
            }
        }

        @Nested
        @DisplayName("When we call loadDevelopersForGame")
        inner class loadDevelopersForGameCalled {

            var observer: TestObserver<List<Company>>? = null
            val game = makeGameRemote()
            val result = makeCompanyList()

            @BeforeEach
            internal fun setUp() {
                whenever(service.loadGame(10, "id,name,slug,url,created_at,updated_at,developers", "developers")).thenReturn(Single.just(listOf(game)))
                whenever(mapper.mapFromModel(game.developers)).thenReturn(result)
                observer = repositoryImpl?.loadDevelopersForGame(10)?.test()
            }

            @Test
            @DisplayName("Then should request game")
            fun requestsGame() {
                verify(service).loadGame(10, "id,name,slug,url,created_at,updated_at,developers", "developers")
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                with(observer) {
                    this?.assertValueCount(1)
                    this?.assertComplete()
                    this?.assertNoErrors()
                    this?.assertValue(result)
                }
            }
        }

        @Nested
        @DisplayName("When we call loadPublishersForGame")
        inner class loadPublishersForGameCalled {

            var observer: TestObserver<List<Company>>? = null
            val game = makeGameRemote()
            val result = makeCompanyList()

            @BeforeEach
            internal fun setUp() {
                whenever(service.loadGame(10, "id,name,slug,url,created_at,updated_at,publishers", "publishers")).thenReturn(Single.just(listOf(game)))
                whenever(mapper.mapFromModel(game.publishers)).thenReturn(result)
                observer = repositoryImpl?.loadPublishersForGame(10)?.test()
            }

            @Test
            @DisplayName("Then should request game")
            fun requestsGame() {
                verify(service).loadGame(10, "id,name,slug,url,created_at,updated_at,publishers", "publishers")
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                with(observer) {
                    this?.assertValueCount(1)
                    this?.assertComplete()
                    this?.assertNoErrors()
                    this?.assertValue(result)
                }
            }
        }

        @Nested
        @DisplayName("When we call saveDeveloperForGame")
        inner class saveDeveloperForGameCalled {

            val entity1 = makeCompany()
            var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                observer = repositoryImpl?.saveDeveloperForGame(10, entity1)?.test()
            }

            @Test
            @DisplayName("Then should emit notAllowedException")
            fun throwsError() {
                assertNotNull(observer)
                with(observer) {
                    this!!.assertNotComplete()
                    assertNoValues()
                    assertError(Throwable::class.java)
                }
            }
        }

        @Nested
        @DisplayName("When we call savePublisherForGame")
        inner class savePublisherForGameCalled {

            val entity1 = makeCompany()
            var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                observer = repositoryImpl?.savePublisherForGame(10, entity1)?.test()
            }

            @Test
            @DisplayName("Then should emit notAllowedException")
            fun throwsError() {
                assertNotNull(observer)
                with(observer) {
                    this!!.assertNotComplete()
                    assertNoValues()
                    assertError(Throwable::class.java)
                }
            }
        }
    }
}