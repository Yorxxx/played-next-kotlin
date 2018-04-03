package com.piticlistudio.playednext.data.repository.datasource.giantbomb

import android.arch.persistence.room.EmptyResultSetException
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombEntityResponse
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombGame
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombListResponse
import com.piticlistudio.playednext.data.entity.mapper.datasources.game.GiantbombGameMapper
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers

/**
 * Created by e-jegi on 4/3/2018.
 */
internal class GiantbombGameDatasourceRepositoryImplTest {

    @Nested
    @DisplayName("Given a GiantbombGameDatasourceRepositoryImpl instance")
    inner class GameRemoteImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        val service: GiantbombService = mock()
        val mapper: GiantbombGameMapper = mock()

        private var repositoryImpl: GiantbombGameDatasourceRepositoryImpl? = null

        @BeforeEach
        fun setup() {
            reset(service, mapper)
            repositoryImpl = GiantbombGameDatasourceRepositoryImpl(service, mapper)
        }

        @Nested
        @DisplayName("When we call load")
        inner class Load {

            var result: TestSubscriber<Game>? = null
            val model = GameFactory.makeGiantbombGame()
            val entity = GameFactory.makeGame()

            @BeforeEach
            fun setup() {
                val response = GiantbombEntityResponse<GiantbombGame>(
                        error = "OK",
                        limit = 1,
                        offset = 0,
                        number_of_page_results = 1,
                        number_of_total_results = 1,
                        status_code = 1,
                        results = model)
                val flowable = Single.create<GiantbombEntityResponse<GiantbombGame>> { it.onSuccess(response) }
                whenever(service.fetchGame(10)).thenReturn(flowable)
                whenever(mapper.mapFromDataLayer(model)).thenReturn(entity)
            }

            @Test
            @DisplayName("Then should request service")
            fun serviceIsCalled() {
                result = repositoryImpl?.load(10)?.test()

                verify(service).fetchGame(10)
            }

            @Test
            @DisplayName("Then should map service response")
            fun mapIsCalled() {
                result = repositoryImpl?.load(10)?.test()

                verify(mapper).mapFromDataLayer(model)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                result = repositoryImpl?.load(10)?.test()

                assertNotNull(result)
                result?.apply {
                    assertValueCount(1)
                    assertComplete()
                    assertNoErrors()
                    assertValue(entity)
                }
            }

            @Nested
            @DisplayName("And returns error code different than OK")
            inner class ErrorResponse {

                @BeforeEach
                fun setup() {
                    val response = GiantbombEntityResponse<GiantbombGame>(
                            error = "FOO",
                            limit = 1,
                            offset = 0,
                            number_of_page_results = 1,
                            number_of_total_results = 1,
                            status_code = 1,
                            results = model)
                    val flowable = Single.create<GiantbombEntityResponse<GiantbombGame>> { it.onSuccess(response) }
                    whenever(service.fetchGame(10)).thenReturn(flowable)
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    result = repositoryImpl?.load(10)?.test()

                    result?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError { it is GiantbombServiceException && it.code == 1 && it.message == "FOO" }
                    }
                }
            }

            @Nested
            @DisplayName("And returns status code different than 1")
            inner class StatusCodeResponse {

                @BeforeEach
                fun setup() {
                    val response = GiantbombEntityResponse<GiantbombGame>(
                            error = "OK",
                            limit = 1,
                            offset = 0,
                            number_of_page_results = 1,
                            number_of_total_results = 1,
                            status_code = 100,
                            results = model)
                    val flowable = Single.create<GiantbombEntityResponse<GiantbombGame>> { it.onSuccess(response) }
                    whenever(service.fetchGame(10)).thenReturn(flowable)
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    result = repositoryImpl?.load(10)?.test()

                    result?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError { it is GiantbombServiceException && it.code == 100 && it.message == "OK" }
                    }
                }
            }

            @Nested
            @DisplayName("And returns null results")
            inner class EmptyResultsResponse {

                @BeforeEach
                fun setup() {
                    val response = GiantbombEntityResponse<GiantbombGame>(
                            error = "OK",
                            limit = 1,
                            offset = 0,
                            number_of_page_results = 1,
                            number_of_total_results = 1,
                            status_code = 1,
                            results = null)
                    val flowable = Single.create<GiantbombEntityResponse<GiantbombGame>> { it.onSuccess(response) }
                    whenever(service.fetchGame(10)).thenReturn(flowable)
                }

                @Test
                @DisplayName("Then should emit EmptyResultSetException")
                fun emitsError() {
                    result = repositoryImpl?.load(10)?.test()

                    result?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError { it is EmptyResultSetException }
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call search")
        inner class Search {

            private val model = GameFactory.makeGiantbombGame()
            private val model2 = GameFactory.makeGiantbombGame()
            private val entity1 = GameFactory.makeGame()
            private val entity2 = GameFactory.makeGame()
            private var result: TestSubscriber<List<Game>>? = null

            @BeforeEach
            fun setup() {
                val response = GiantbombListResponse<GiantbombGame>(
                        error = "OK",
                        limit = 1,
                        offset = 0,
                        number_of_page_results = 1,
                        number_of_total_results = 1,
                        status_code = 1,
                        results = listOf<GiantbombGame>(model, model2))
                val flowable = Single.create<GiantbombListResponse<GiantbombGame>> { it.onSuccess(response) }

                whenever(service.searchGames(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(flowable)
                whenever(mapper.mapFromDataLayer(model)).thenReturn(entity1)
                whenever(mapper.mapFromDataLayer(model2)).thenReturn(entity2)
            }

            @Test
            @DisplayName("Then should request service with correct params")
            fun serviceIsCalled() {
                result = repositoryImpl?.search("zelda", 5, 15)?.test()

                verify(service).searchGames(
                        resources = "game",
                        format = "json",
                        fields = "id,image,name,platforms,date_added,date_last_updated",
                        query = "zelda",
                        limit = 15,
                        offset = 5)
            }

            @Test
            @DisplayName("Then maps result into data model")
            fun mapIsCalled() {
                result = repositoryImpl?.search("zelda", 5, 15)?.test()

                verify(mapper).mapFromDataLayer(model)
                verify(mapper).mapFromDataLayer(model2)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutError() {
                result = repositoryImpl?.search("zelda", 5, 15)?.test()

                assertNotNull(result)
                result?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertValueCount(1)
                    assertValue(listOf(entity1, entity2))
                }
            }

            @Nested
            @DisplayName("And returns error code different than OK")
            inner class ErrorResponse {

                @BeforeEach
                fun setup() {
                    val response = GiantbombListResponse<GiantbombGame>(
                            error = "FOO",
                            limit = 1,
                            offset = 0,
                            number_of_page_results = 1,
                            number_of_total_results = 1,
                            status_code = 1,
                            results = listOf())
                    val flowable = Single.create<GiantbombListResponse<GiantbombGame>> { it.onSuccess(response) }
                    whenever(service.searchGames(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(flowable)
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    result = repositoryImpl?.search("zelda", 5, 15)?.test()

                    result?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError { it is GiantbombServiceException && it.code == 1 && it.message == "FOO" }
                    }
                }
            }

            @Nested
            @DisplayName("And returns status code different than 1")
            inner class StatusCodeResponse {

                @BeforeEach
                fun setup() {
                    val response = GiantbombListResponse<GiantbombGame>(
                            error = "OK",
                            limit = 1,
                            offset = 0,
                            number_of_page_results = 1,
                            number_of_total_results = 1,
                            status_code = 100,
                            results = listOf())
                    val flowable = Single.create<GiantbombListResponse<GiantbombGame>> { it.onSuccess(response) }
                    whenever(service.searchGames(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(flowable)
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    result = repositoryImpl?.search("zelda", 5, 15)?.test()

                    result?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError { it is GiantbombServiceException && it.code == 100 && it.message == "OK" }
                    }
                }
            }

            @Nested
            @DisplayName("And returns empty results")
            inner class EmptyResultsResponse {

                @BeforeEach
                fun setup() {
                    val response = GiantbombListResponse<GiantbombGame>(
                            error = "OK",
                            limit = 1,
                            offset = 0,
                            number_of_page_results = 1,
                            number_of_total_results = 1,
                            status_code = 1,
                            results = listOf())
                    val flowable = Single.create<GiantbombListResponse<GiantbombGame>> { it.onSuccess(response) }
                    whenever(service.searchGames(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(flowable)
                }

                @Test
                @DisplayName("Then should emit EmptyResultSetException")
                fun emitsError() {
                    result = repositoryImpl?.search("zelda", 5, 15)?.test()

                    result?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError { it is EmptyResultSetException }
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call save")
        inner class Save {

            private val entity1 = GameFactory.makeGame()
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
    }
}