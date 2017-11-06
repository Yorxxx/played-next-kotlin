package com.piticlistudio.playednext.data.repository.datasource.net

import android.arch.persistence.room.EmptyResultSetException
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDTOMapper
import com.piticlistudio.playednext.data.entity.net.GameDTO
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameRemote
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
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

        private var repositoryImpl: GameRemoteImpl? = null

        @BeforeEach
        fun setup() {
            MockitoAnnotations.initMocks(this)
            repositoryImpl = GameRemoteImpl(service, mapper)
        }

        @Nested
        @DisplayName("When we call load")
        inner class Load {

            var result: TestSubscriber<Game>? = null
            val model = makeGameRemote()
            val entity = makeGame()

            @BeforeEach
            fun setup() {
                val flowable = Single.create<List<GameDTO>> { it.onSuccess(listOf(model))  }
                whenever(service.loadGame(10)).thenReturn(flowable)
                whenever(mapper.mapFromModel(model)).thenReturn(entity)
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
                verify(mapper).mapFromModel(model)
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
                    val flowable = Single.create<List<GameDTO>> { it.onSuccess(listOf())  }
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

            val model = makeGameRemote()
            val model2 = makeGameRemote()
            val response = listOf(model, model2)
            val entity1 = makeGame()
            val entity2 = makeGame()
            var result: TestSubscriber<List<Game>>? = null

            @BeforeEach
            fun setup() {
                val flowable = Single.create<List<GameDTO>> { it.onSuccess(listOf(model, model2))  }
                whenever(service.searchGames(0, "query", "*", 20)).thenReturn(flowable)
                whenever(mapper.mapFromModel(model)).thenReturn(entity1)
                whenever(mapper.mapFromModel(model2)).thenReturn(entity2)
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

            val entity1 = makeGame()
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