package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.persistence.room.EmptyResultSetException
import android.database.sqlite.SQLiteConstraintException
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.data.entity.dao.GameWithRelationalData
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDaoMapper
import com.piticlistudio.playednext.data.repository.datasource.CompanyDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.dao.game.GameDaoService
import com.piticlistudio.playednext.data.repository.datasource.dao.game.GameLocalImpl
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompany
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameCache
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameWithRelationalData
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
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class GameLocalImplTest {

    @Nested
    @DisplayName("Given GameLocalImpl instance")
    inner class LocalImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock
        private lateinit var daoService: GameDaoService
        @Mock private lateinit var mapper: DaoModelMapper<GameWithRelationalData, Game>
        @Mock private lateinit var companydao: CompanyDatasourceRepository

        private lateinit var repository: GameLocalImpl

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = GameLocalImpl(daoService, companydao, mapper)
        }

        @Nested
        @DisplayName("When load is called")
        inner class LoadIsCalled {

            private var observer: TestSubscriber<Game>? = null
            private val model = makeGameWithRelationalData()
            private val entity = makeGame().apply {
                developers = null
            }

            @BeforeEach
            internal fun setUp() {
                val flowable = Flowable.create<List<GameWithRelationalData>>({
                    it.onNext(listOf(model))
                    it.onNext(listOf(model))
                }, BackpressureStrategy.MISSING)
                whenever(daoService.loadById(10)).thenReturn(flowable)
                whenever(mapper.mapFromDao(model)).thenReturn(entity)
                whenever(companydao.load(anyInt())).thenReturn(Single.just(makeCompany()))
                observer = repository.load(10).test()
            }

            @Test
            @DisplayName("Then should request DAO")
            fun daoIsCalled() {
                verify(daoService).loadById(10)
            }

            @Test
            @DisplayName("Then should request developers")
            fun requestdevelopers() {
                model.companyIdList?.forEach {
                    verify(companydao).load(it.companyId)
                }
            }

            @Test
            @DisplayName("Then should map daoService result")
            fun mapIsCalled() {
                verify(mapper).mapFromDao(model)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNotComplete()
                    assertValueCount(1)
                    assertNoErrors()
                    assertValue(entity)
                    assertValue {
                        it.developers != null
                    }
                    assertValue {
                        with(it.developers!!) {
                            size == model.companyIdList!!.size
                        }
                    }
                }
            }

            @Nested
            @DisplayName("And there are no results in database")
            inner class NoResults {

                @BeforeEach
                internal fun setUp() {
                    val flowable = Flowable.create<List<GameWithRelationalData>>({ it.onNext(listOf()) }, BackpressureStrategy.MISSING)
                    whenever(daoService.loadById(anyLong())).thenReturn(flowable)
                    observer = repository.load(10).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withoutErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError { it is EmptyResultSetException }
                    }
                }

            }
        }

        /*@Nested
        @DisplayName("When save is called")
        inner class SaveIsCalled {

            private val source = makeGame()
            private val data = makeGameCache()
            private var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapIntoDao(source)).thenReturn(data)
                observer = repository.save(source).test()
            }

            @Test
            @DisplayName("Then maps entity into Dao model")
            fun mapsEntity() {
                verify(mapper).mapIntoDao(source)
            }

            @Test
            @DisplayName("Then inserts into DAO")
            fun isInserted() {
                verify(daoService).insert(data)
            }

            @Test
            @DisplayName("Then should not update")
            fun updateNotCalled() {
                verify(daoService, never()).update(data)
            }

            @Test
            @DisplayName("Then emits completion")
            fun emitsComplete() {
                assertNotNull(observer)
                observer?.apply {
                    assertComplete()
                    assertNoValues()
                    assertNoErrors()
                }
            }

            @Nested
            @DisplayName("and data is already stored")
            inner class AlreadyStored {

                @BeforeEach
                internal fun setUp() {
                    whenever(daoService.insert(data)).thenThrow(SQLiteConstraintException())
                    observer = repository.save(source).test()
                }

                @Test
                @DisplayName("Then should update")
                fun updateNotCalled() {
                    verify(daoService).update(data)
                }

                @Test
                @DisplayName("Then emits completion")
                fun emitsComplete() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertComplete()
                        assertNoValues()
                        assertNoErrors()
                    }
                }
            }
        }*/

        /*@Nested
        @DisplayName("When search is called")
        inner class SearchCalled {

            private val data1 = makeGameCache()
            private val data2 = makeGameCache()
            private val data3 = makeGameCache()
            private var observer: TestSubscriber<List<Game>>? = null
            private val entity1 = makeGame()
            private val entity2 = makeGame()
            private val entity3 = makeGame()

            @BeforeEach
            internal fun setUp() {
                val flowable = Flowable.create<List<GameDao>>({
                    it.onNext(listOf(data1, data2))
                    it.onNext(listOf(data1, data2))
                    it.onNext(listOf(data1, data2, data3))
                }, BackpressureStrategy.MISSING)
                whenever(daoService.findByName(anyString())).thenReturn(flowable)
                whenever(mapper.mapFromEntity(data1)).thenReturn(entity1)
                whenever(mapper.mapFromEntity(data2)).thenReturn(entity2)
                whenever(mapper.mapFromEntity(data3)).thenReturn(entity3)
                observer = repository.search("foo", 0, 10).test()
            }

            @Test
            @DisplayName("Then searches on DAO")
            fun daoIsCalled() {
                verify(daoService).findByName("foo")
            }

            @Test
            @DisplayName("Then maps result into GameEntities")
            fun isMapped() {
                verify(mapper, times(2)).mapFromEntity(data1)
                verify(mapper, times(2)).mapFromEntity(data2)
                verify(mapper).mapFromEntity(data3)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertValueCount(2)
                    assertNotComplete()
                    assertValueAt(0, { it.containsAll(listOf(entity1, entity2)) })
                    assertValueAt(1, { it.containsAll(listOf(entity1, entity2, entity3)) })
                }
            }
        }*/
    }
}