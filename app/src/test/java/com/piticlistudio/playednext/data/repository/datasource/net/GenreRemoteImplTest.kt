package com.piticlistudio.playednext.data.repository.datasource.net

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.datasources.GenreDTOMapper
import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.GameFactory
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenre
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

internal class GenreRemoteImplTest {

    @Nested
    @DisplayName("Given GenreRemoteImpl instance")
    inner class GenreRemoteImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock lateinit var service: IGDBService
        @Mock lateinit var mapper: GenreDTOMapper

        private var repositoryImpl: GenreRemoteImpl? = null
        private val gameId = 10

        @BeforeEach
        fun setup() {
            MockitoAnnotations.initMocks(this)
            repositoryImpl = GenreRemoteImpl(service, mapper)
        }

        @Nested
        @DisplayName("When we call save")
        inner class Save {

            private val entity1 = makeGenre()
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
        inner class LoadForGameCalled {

            var observer: TestObserver<List<Genre>>? = null
            val game = GameFactory.makeGameRemote()
            val result = randomListOf{ makeGenre() }

            @BeforeEach
            internal fun setUp() {
                whenever(service.loadGame(gameId, "id,name,slug,url,created_at,updated_at,genres", "genres")).thenReturn(Single.just(listOf(game)))
                whenever(mapper.mapFromModel(game.genres)).thenReturn(result)
                observer = repositoryImpl?.loadForGame(gameId)?.test()
            }

            @Test
            @DisplayName("Then should request game")
            fun requestsGame() {
                verify(service).loadGame(gameId, "id,name,slug,url,created_at,updated_at,genres", "genres")
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
        inner class InsertGameGenreCalled {

            private val entity1 = makeGenre()
            var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                observer = repositoryImpl?.insertGameGenre(gameId, entity1)?.test()
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