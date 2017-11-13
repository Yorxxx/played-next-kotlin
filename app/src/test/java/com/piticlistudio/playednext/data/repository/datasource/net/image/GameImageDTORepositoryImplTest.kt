package com.piticlistudio.playednext.data.repository.datasource.net.image

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.mapper.datasources.image.ImageDTOMapper
import com.piticlistudio.playednext.data.repository.datasource.net.IGDBService
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameRemote
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGameImage
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertNotNull

internal class GameImageDTORepositoryImplTest {

    @Nested
    @DisplayName("Given GameImageDTORepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: GameImageDTORepositoryImpl
        @Mock private lateinit var service: IGDBService
        @Mock private lateinit var mapper: ImageDTOMapper

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = GameImageDTORepositoryImpl(service, mapper)
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class LoadForGameCalled {

            private val gameId = 100
            private var observer: TestSubscriber<List<GameImage>>? = null
            private val game = makeGameRemote()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromModel(any())).thenReturn(makeGameImage())
                whenever(service.loadGame(anyInt(), anyString(), anyOrNull())).thenReturn(Single.just(listOf(game)))
                observer = repository.loadForGame(gameId).test()
            }

            @Test
            @DisplayName("Then should request service")
            fun serviceIsRequested() {
                verify(service).loadGame(gameId, "id,name,slug,url,created_at,updated_at,screenshots", null)
            }

            @Test
            @DisplayName("Then should map response")
            fun mapsResult() {
                verify(mapper, times(game.screenshots?.size!!)).mapFromModel(any())
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertValueCount(1)
                    assertComplete()
                    assertValue { it.size == game.screenshots?.size }
                }
            }

            @Nested
            @DisplayName("And request fails")
            inner class RequestFails {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(service.loadGame(anyInt(), anyString(), anyOrNull())).thenReturn(Single.error(error))
                    reset(mapper)
                    observer = repository.loadForGame(gameId).test()
                }

                @Test
                @DisplayName("Then does not map")
                fun doesNotMap() {
                    verifyZeroInteractions(mapper)
                }

                @Test
                @DisplayName("Then emits error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertNotComplete()
                        assertError(error)
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call saveForGame")
        inner class SaveForGameCalled {

            private var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                observer = repository.saveForGame(anyInt(), makeGameImage()).test()
            }

            @Test
            @DisplayName("Then emits error")
            fun emitsError() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoValues()
                    assertNotComplete()
                    assertError(Throwable::class.java)
                }
            }
        }
    }
}