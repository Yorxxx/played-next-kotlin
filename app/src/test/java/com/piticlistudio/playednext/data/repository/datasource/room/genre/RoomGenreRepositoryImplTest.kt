package com.piticlistudio.playednext.data.repository.datasource.room.genre

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.mapper.datasources.genre.RoomGenreMapper
import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeRoomGenre
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RoomGenreRepositoryImplTest {

    @Nested
    @DisplayName("Given a RoomGenreRepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: RoomGenreRepositoryImpl
        private val dao: RoomGenreService = mock()
        private val mapper: RoomGenreMapper = mock()

        @BeforeEach
        internal fun setUp() {
            reset(dao, mapper)
            repository = RoomGenreRepositoryImpl(dao, mapper)
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class LoadCalled {

            private var observer: TestSubscriber<List<Genre>>? = null
            private val source = makeRoomGenre()
            private val result = makeGenre()

            @BeforeEach
            internal fun setUp() {
                whenever(dao.findForGame(10)).thenReturn(Flowable.just(listOf(source)))
                whenever(mapper.mapFromDataLayer(source)).thenReturn(result)
                observer = repository.loadForGame(10).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestRepository() {
                verify(dao).findForGame(10)
            }

            @Test
            @DisplayName("Then should map response")
            fun shouldMap() {
                verify(mapper).mapFromDataLayer(source)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertValueCount(1)
                    assertValue { it.size == 1 && it.contains(result) }
                }
            }
        }

        @Nested
        @DisplayName("When we call saveGenreForGame")
        inner class InsertGameGenreCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makeGenre()
            private val result = makeRoomGenre()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapIntoDataLayerModel(source)).thenReturn(result)
                whenever(dao.insert(result)).thenReturn(10)
                whenever(dao.insertGameGenre(any())).thenReturn(10)
                observer = repository.saveGenreForGame(10, source).test()
            }

            @Test
            @DisplayName("Then should save company")
            fun shouldSaveCompany() {
                verify(dao).insert(result)
            }

            @Test
            @DisplayName("Then should save relation")
            fun shouldRequestDao() {
                verify(dao).insertGameGenre(com.nhaarman.mockito_kotlin.check {
                    assertEquals(it.genreId, source.id)
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
                    reset(dao, mapper)
                    whenever(dao.insert(result)).thenReturn(-1)

                    observer = repository.saveGenreForGame(10, source).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertError(GenreSaveException::class.java)
                        assertNotComplete()
                    }
                }

                @Test
                @DisplayName("Then should not save RoomGameGenre")
                fun shouldNotSaveGameDeveloper() {
                    verify(dao, never()).insertGameGenre(any())
                }
            }

            @Nested
            @DisplayName("And Room fails to save gameGenre")
            inner class RoomPublisherFail {

                @BeforeEach
                internal fun setUp() {
                    reset(dao)
                    whenever(dao.insert(result)).thenReturn(10)
                    whenever(dao.insertGameGenre(any())).thenReturn(-1)

                    observer = repository.saveGenreForGame(10, source).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertError(GameGenreSaveException::class.java)
                        assertNotComplete()
                    }
                }

                @Test
                @DisplayName("Then should have saved company")
                fun shouldHaveSavedCompany() {
                    verify(dao).insert(result)
                }
            }
        }
    }
}