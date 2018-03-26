package com.piticlistudio.playednext.data.repository.datasource.dao

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.datasources.genre.GenreDaoMapper
import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenreDao
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class GenreDaoRepositoryImplTest {

    @Nested
    @DisplayName("Given a GenreDaoRepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: GenreDaoRepositoryImpl
        @Mock
        private lateinit var dao: GenreDaoService
        @Mock
        private lateinit var mapper: GenreDaoMapper

        private val gameId = 10

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = GenreDaoRepositoryImpl(dao, mapper)
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class LoadForGameCalled {

            private var observer: TestObserver<List<Genre>>? = null
            private val source = randomListOf { makeGenreDao() }
            private val result = randomListOf { makeGenre() }

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromModel(source)).thenReturn(result)
                whenever(dao.findForGame(gameId)).thenReturn(Single.just(source))
                observer = repository.loadForGame(gameId).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).findForGame(gameId)
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
        @DisplayName("When we call save")
        inner class SaveCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makeGenre()
            private val daoentity = makeGenreDao()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromEntity(source)).thenReturn(daoentity)
                whenever(dao.insertGameGenre(any())).thenReturn(randomLong())
                observer = repository.save(source).test()
            }

            @Test
            @DisplayName("Then should save genre")
            fun shouldSaveCompany() {
                verify(dao).insert(daoentity)
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
        @DisplayName("When we call insertGameGenre")
        inner class InsertGameGenreCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makeGenre()
            private val daoentity = makeGenreDao()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromEntity(source)).thenReturn(daoentity)
                whenever(dao.insert(daoentity)).thenReturn(randomLong())
                whenever(dao.insertGameGenre(any())).thenReturn(randomLong())
                observer = repository.insertGameGenre(gameId, source).test()
            }

            @Test
            @DisplayName("Then should save genre")
            fun shouldSaveCompany() {
                verify(dao).insert(daoentity)
            }

            @Test
            @DisplayName("Then should save relation")
            fun shouldRequestDao() {
                verify(dao).insertGameGenre(com.nhaarman.mockito_kotlin.check {
                    kotlin.test.assertEquals(it.genreId, source.id)
                    kotlin.test.assertEquals(it.gameId, gameId)
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