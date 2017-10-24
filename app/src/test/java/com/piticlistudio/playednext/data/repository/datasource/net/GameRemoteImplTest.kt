package com.piticlistudio.playednext.data.repository.datasource.net

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDTOMapper
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
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

internal class GameRemoteImplTest {

    @Nested
    @DisplayName("Given a GameRemoteImpl instance")
    inner class GameRemoteImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock lateinit var service: IGDBService
        @Mock lateinit var mapper: GameDTOMapper
        @Mock lateinit var companymapper: CompanyDTOMapper

        private var repositoryImpl: GameRemoteImpl? = null

        @BeforeEach
        fun setup() {
            MockitoAnnotations.initMocks(this)
            repositoryImpl = GameRemoteImpl(service, mapper, companymapper)
        }

        @Nested
        @DisplayName("When we call load")
        inner class Load {

            var result: TestObserver<Game>? = null
            val model = makeGameRemote()
            val entity = makeGame()

            @BeforeEach
            fun setup() {
                val response = listOf(model)

                whenever(service.loadGame(10, "*"))
                        .thenReturn(Single.just(response))
                whenever(mapper.mapFromModel(model)).thenReturn(entity)
                result = repositoryImpl?.load(10)?.test()
            }

            @Test
            @DisplayName("Then should request service")
            fun serviceIsCalled() {
                verify(service).loadGame(10, "*")
            }

            @Test
            @DisplayName("Then should map service response")
            fun mapIsCalled() {
                verify(mapper).mapFromModel(model)
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

            @Nested
            @DisplayName("And returns empty list")
            inner class EmptyResponse {

                @BeforeEach
                fun setup() {
                    whenever(service.loadGame(10, "*"))
                            .thenReturn(Single.just(listOf()))
                    result = repositoryImpl?.load(10)?.test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    with(result) {
                        this?.assertNoValues()
                        this?.assertNotComplete()
                        this?.assertError(Throwable::class.java)
                    }
                }
            }

            @Nested
            @DisplayName("And returns list with multiple items")
            inner class MultipleListResponse {

                @BeforeEach
                fun setup() {
                    val response = listOf(makeGameRemote(), makeGameRemote())
                    whenever(service.loadGame(10, "*"))
                            .thenReturn(Single.just(response))
                    result = repositoryImpl?.load(10)?.test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    with(result) {
                        this?.assertNoValues()
                        this?.assertNotComplete()
                        this?.assertError(Throwable::class.java)
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call search")
        inner class Search {

            val model = makeGameRemote()
            val model2 = makeGameRemote()
            val response = listOf(model, model2)
            val entity1 = makeGame()
            val entity2 = makeGame()
            var result: TestObserver<List<Game>>? = null

            @BeforeEach
            fun setup() {
                whenever(service.searchGames(0, "query", "*", 20))
                        .thenReturn(Single.just(response))
                whenever(mapper.mapFromModel(model))
                        .thenReturn(entity1)
                whenever(mapper.mapFromModel(model2))
                        .thenReturn(entity2)
                result = repositoryImpl?.search("query")?.test()
            }

            @Test
            @DisplayName("Then should request service with correct params")
            fun serviceIsCalled() {
                verify(service).searchGames(0, "query", "*", 20)
            }

            @Test
            @DisplayName("Then maps result into data model")
            fun mapIsCalled() {
                verify(mapper).mapFromModel(model)
                verify(mapper).mapFromModel(model2)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutError() {
                assertNotNull(result)
                with(result) {
                    this!!.assertNoErrors()
                    assertComplete()
                    assertValueCount(1)
                    assertValue(listOf(entity1, entity2))
                }
            }
        }

        @Nested
        @DisplayName("When we call save")
        inner class Save {

            val entity1 = makeGame()
            var                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              observer: TestObserver<Void>? = null

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