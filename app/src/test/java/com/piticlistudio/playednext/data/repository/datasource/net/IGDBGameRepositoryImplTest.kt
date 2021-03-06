package com.piticlistudio.playednext.data.repository.datasource.net

import android.arch.persistence.room.EmptyResultSetException
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.datasources.game.IGDBGameMapper
import com.piticlistudio.playednext.data.entity.igdb.IGDBGame
import com.piticlistudio.playednext.data.repository.datasource.igdb.IGDBGameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.igdb.IGDBService
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeIGDBGame
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class IGDBGameRepositoryImplTest {

    @Nested
    @DisplayName("Given a IGDBGameRepositoryImpl instance")
    inner class IGDBGameRepositoryImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock lateinit var service: IGDBService
        @Mock lateinit var mapper: IGDBGameMapper

        private var repositoryImpl: IGDBGameRepositoryImpl? = null

        @BeforeEach
        fun setup() {
            MockitoAnnotations.initMocks(this)
            repositoryImpl = IGDBGameRepositoryImpl(service, mapper)
        }

        @Nested
        @DisplayName("When we call load")
        inner class Load {

            var result: TestSubscriber<Game>? = null
            val model = makeIGDBGame()
            val entity = makeGame()

            @BeforeEach
            fun setup() {
                val flowable = Single.create<List<IGDBGame>> { it.onSuccess(listOf(model))  }
                whenever(service.loadGame(10)).thenReturn(flowable)
                whenever(mapper.mapFromDataLayer(model)).thenReturn(entity)
                result = repositoryImpl?.load(10)?.test()
            }

            @Test
            @DisplayName("Then should request service")
            fun serviceIsCalled() {
                verify(service).loadGame(10)
            }

            @Test
            @DisplayName("Then should map service response")
            fun mapIsCalled() {
                verify(mapper).mapFromDataLayer(model)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(result)
                result?.apply {
                    assertValueCount(1)
                    assertComplete()
                    assertNoErrors()
                    assertValue(entity)
                }
            }

            @Nested
            @DisplayName("And returns empty list")
            inner class EmptyResponse {

                @BeforeEach
                fun setup() {
                    val flowable = Single.create<List<IGDBGame>> { it.onSuccess(listOf())  }
                    whenever(service.loadGame(10)).thenReturn(flowable)
                    result = repositoryImpl?.load(10)?.test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
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

            private val model = makeIGDBGame()
            private val model2 = makeIGDBGame()
            private val entity1 = makeGame()
            private val entity2 = makeGame()
            private var result: TestSubscriber<List<Game>>? = null

            @BeforeEach
            fun setup() {
                val flowable = Single.create<List<IGDBGame>> { it.onSuccess(listOf(model, model2))  }
                whenever(service.searchGames(anyInt(), anyString(), anyString(), anyInt())).thenReturn(flowable)
                whenever(mapper.mapFromDataLayer(model)).thenReturn(entity1)
                whenever(mapper.mapFromDataLayer(model2)).thenReturn(entity2)
                result = repositoryImpl?.search("query", 5, 15)?.test()
            }

            @Test
            @DisplayName("Then should request service with correct params")
            fun serviceIsCalled() {
                verify(service).searchGames(5, "query", "id,name,slug,url,summary,franchise,rating,storyline,popularity,total_rating,total_rating_count,rating_count,screenshots,cover,updated_at,created_at", 15)
            }

            @Test
            @DisplayName("Then maps result into data model")
            fun mapIsCalled() {
                verify(mapper).mapFromDataLayer(model)
                verify(mapper).mapFromDataLayer(model2)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutError() {
                assertNotNull(result)
                result?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertValueCount(1)
                    assertValue(listOf(entity1, entity2))
                }
            }
        }

        @Nested
        @DisplayName("When we call save")
        inner class Save {

            private val entity1 = makeGame()
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