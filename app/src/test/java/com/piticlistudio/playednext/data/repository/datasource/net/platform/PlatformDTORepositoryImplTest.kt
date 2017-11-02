package com.piticlistudio.playednext.data.repository.datasource.net.platform

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.PlatformDTOMapper
import com.piticlistudio.playednext.data.repository.datasource.net.IGDBService
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.test.factory.GameFactory
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatformDTO
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatformList
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.Single
import io.reactivex.observers.TestObserver
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

internal class PlatformDTORepositoryImplTest {

    @Nested
    @DisplayName("Given PlatformDTORepositoryImpl instance")
    inner class PlatformDTORepositoryImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock lateinit var service: IGDBService
        @Mock lateinit var mapper: PlatformDTOMapper

        private var repositoryImpl: PlatformDTORepositoryImpl? = null
        private val gameId = 10

        @BeforeEach
        fun setup() {
            MockitoAnnotations.initMocks(this)
            repositoryImpl = PlatformDTORepositoryImpl(service, mapper)
        }

        @Nested
        @DisplayName("When we call save")
        inner class Save {

            val entity1 = makePlatform()
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
        @DisplayName("When we call loadForGame")
        inner class loadForGameCalled {

            var observer: TestObserver<List<Platform>>? = null
            val game = GameFactory.makeGameRemote()
            val result = makePlatformList()

            @BeforeEach
            internal fun setUp() {
                whenever(service.loadGame(gameId, "id,name,slug,url,created_at,updated_at,platforms", "platforms")).thenReturn(Single.just(listOf(game)))
                whenever(mapper.mapFromModel(game.platforms)).thenReturn(result)
                observer = repositoryImpl?.loadForGame(gameId)?.test()
            }

            @Test
            @DisplayName("Then should request game")
            fun requestsGame() {
                verify(service).loadGame(gameId, "id,name,slug,url,created_at,updated_at,platforms", "platforms")
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
        @DisplayName("When we call insertGameGenre")
        inner class insertGameGenreCalled {

            val entity1 = makePlatform()
            var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                observer = repositoryImpl?.saveForGame(gameId, entity1)?.test()
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
        @DisplayName("When we call load")
        inner class loadCalled {

            var observer: TestObserver<Platform>? = null
            val platformDTO = makePlatformDTO()
            val result = makePlatform()

            @BeforeEach
            internal fun setUp() {
                whenever(service.loadPlatform(anyInt(), anyString())).thenReturn(Single.just(platformDTO))
                whenever(mapper.mapFromModel(platformDTO)).thenReturn(result)
                observer = repositoryImpl?.load(gameId)?.test()
            }

            @Test
            @DisplayName("Then should request platform")
            fun requestsGame() {
                verify(service).loadPlatform(gameId, "*")
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
    }
}