package com.piticlistudio.playednext.data.repository.datasource.dao.relation

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.dao.GameRelationDao
import com.piticlistudio.playednext.data.entity.mapper.datasources.relation.RelationDaoMapper
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.test.factory.DataFactory
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelationDao
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class RelationDaoRepositoryImplTest {

    @Nested
    @DisplayName("Given a RelationDaoRepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: RelationDaoRepositoryImpl
        @Mock private lateinit var dao: RelationDaoService
        @Mock private lateinit var mapper: RelationDaoMapper

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = RelationDaoRepositoryImpl(dao, mapper)
        }

        @Nested
        @DisplayName("When we call save")
        inner class SaveCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makeGameRelation()
            private val result = makeGameRelationDao()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromEntity(source)).thenReturn(result)
                whenever(dao.insert(result)).thenReturn(DataFactory.randomLong())
                observer = repository.save(source).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).insert(result)
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
        @DisplayName("When we call loadForGameAndPlatform")
        inner class LoadForGameAndPlatformCalled {

            private var observer: TestSubscriber<List<GameRelation>>? = null
            private val daomodel1 = makeGameRelationDao()
            private val entity1 = makeGameRelation()
            private val gameId = 10
            private val platformId = 5

            @BeforeEach
            internal fun setUp() {
                val flowable = Flowable.create<List<GameRelationDao>>({
                    it.onNext(listOf(daomodel1))
                    it.onNext(listOf(daomodel1))
                }, BackpressureStrategy.MISSING)
                whenever(mapper.mapFromModel(daomodel1)).thenReturn(entity1)
                whenever(dao.findForGameAndPlatform(gameId, platformId)).thenReturn(flowable)
                observer = repository.loadForGameAndPlatform(gameId, platformId).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).findForGameAndPlatform(gameId, platformId)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertNotComplete()
                    assertValueCount(1)
                    assertValue { it.contains(entity1) && it.size == 1 }
                }
            }

            @Nested
            @DisplayName("And there are no results in DAO")
            inner class EmptyDatabase {

                @BeforeEach
                internal fun setUp() {
                    val flowable = Flowable.create<List<GameRelationDao>>({
                        it.onNext(listOf())
                    }, BackpressureStrategy.MISSING)
                    whenever(dao.findForGameAndPlatform(gameId, platformId)).thenReturn(flowable)
                    observer = repository.loadForGameAndPlatform(gameId, platformId).test()
                }

                @Test
                @DisplayName("Then emits empty list")
                fun emptyList() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNotComplete()
                        assertValueCount(1)
                        assertNoErrors()
                        assertValue(listOf())
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class LoadForGameCalled {

            private var observer: TestSubscriber<List<GameRelation>>? = null
            private val gameId = 100
            private val emission1 = listOf(makeGameRelationDao(), makeGameRelationDao())
            private val emission2 = listOf(makeGameRelationDao(), makeGameRelationDao(), makeGameRelationDao())

            @BeforeEach
            internal fun setUp() {
                val flowable = Flowable.create<List<GameRelationDao>>({
                    it.onNext(emission1)
                    it.onNext(emission2)
                }, BackpressureStrategy.MISSING)
                whenever(dao.findForGame(anyInt())).thenReturn(flowable)

                whenever(mapper.mapFromModel(any())).thenReturn(makeGameRelation())
                observer = repository.loadForGame(gameId).test()
            }

            @Test
            @DisplayName("Then should request dao")
            fun shouldRequestDao() {
                verify(dao).findForGame(gameId)
            }

            @Test
            @DisplayName("Then should map result")
            fun shouldMapResults() {
                verify(mapper, times(emission1.size + emission2.size)).mapFromModel(any())
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertNotComplete()
                    assertValueCount(2)
                    assertValueAt(0, { it.size == emission1.size })
                    assertValueAt(1, { it.size == emission2.size })
                }
            }
        }
    }
}