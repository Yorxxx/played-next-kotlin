package com.piticlistudio.playednext.data.repository.datasource.dao.image

import android.database.sqlite.SQLiteConstraintException
import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.dao.ScreenshotDao
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGameImage
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGameImageDao
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertNotNull

internal class GameImageDaoRepositoryImplTest {

    @Nested
    @DisplayName("Given a GameImageDaoRepositoryImpl instance")
    inner class Instance {

        @Mock private lateinit var dao: GameImagesDaoService
        @Mock private lateinit var mapper: DaoModelMapper<ScreenshotDao, GameImage>
        private lateinit var repository: GameImageDaoRepositoryImpl

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = GameImageDaoRepositoryImpl(dao, mapper)
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class LoadForGameCalled {

            private val gameId = 100
            private var observer: TestSubscriber<List<GameImage>>? = null

            @BeforeEach
            internal fun setUp() {
                val flowable = Flowable.create<List<ScreenshotDao>>({ it.onNext(listOf(makeGameImageDao(), makeGameImageDao())) }, BackpressureStrategy.MISSING)
                whenever(dao.findForGame(anyInt())).thenReturn(flowable)
                whenever(mapper.mapFromDao(any())).thenReturn(makeGameImage())
                observer = repository.loadForGame(gameId).test()
            }

            @Test
            @DisplayName("Then should request dao")
            fun daoIsCalled() {
                verify(dao).findForGame(gameId)
            }

            @Test
            @DisplayName("Then should map result")
            fun shouldMap() {
                verify(mapper, times(2)).mapFromDao(any())
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertNotComplete()
                    assertValueCount(1)
                    assertValue { it.size == 2 }
                }
            }

            @Nested
            @DisplayName("And dao emits error")
            inner class DaoFails {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(dao.findForGame(anyInt())).thenReturn(Flowable.error(error))
                    reset(mapper)
                    observer = repository.loadForGame(gameId).test()
                }

                @Test
                @DisplayName("Then should not request mapping")
                fun noMapping() {
                    verifyZeroInteractions(mapper)
                }

                @Test
                @DisplayName("Then should emit error")
                fun withErrors() {
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

            private val gameId = 100
            private val data = makeGameImage()
            private var observer: TestObserver<Void>? = null
            private val daoData = makeGameImageDao()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapIntoDao(any())).thenReturn(daoData)
                whenever(dao.insert(any())).thenReturn(anyLong())
                observer = repository.saveForGame(gameId, data).test()
            }

            @Test
            @DisplayName("Then should map result")
            fun mapIsCalled() {
                verify(mapper).mapIntoDao(data)
            }

            @Test
            @DisplayName("Then request dao")
            fun daoIsCalled() {
                verify(dao).insert(daoData)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertNoValues()
                    assertComplete()
                }
            }

            @Nested
            @DisplayName("And dao fails")
            inner class InsertFails {

                @BeforeEach
                internal fun setUp() {
                    whenever(dao.insert(any())).thenThrow(SQLiteConstraintException())
                    observer = repository.saveForGame(gameId, data).test()
                }

                @Test
                @DisplayName("Then emits error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertNotComplete()
                        assertError { it is SQLiteConstraintException }
                    }
                }
            }
        }
    }
}