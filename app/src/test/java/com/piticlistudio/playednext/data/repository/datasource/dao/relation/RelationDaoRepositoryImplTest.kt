package com.piticlistudio.playednext.data.repository.datasource.dao.relation

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.dao.RelationWithGameAndPlatform
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.PlatformDatasourceRepository
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.test.factory.DataFactory
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelationWithGameAndPlatform
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
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
        @Mock private lateinit var mapper: DaoModelMapper<RelationWithGameAndPlatform, GameRelation>
        @Mock private lateinit var gamerepo: GameDatasourceRepository
        @Mock private lateinit var platformrepo: PlatformDatasourceRepository

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = RelationDaoRepositoryImpl(dao, gamerepo, platformrepo, mapper)
        }

        @Nested
        @DisplayName("When we call save")
        inner class SaveCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makeGameRelation()
            private val result = makeGameRelationWithGameAndPlatform()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapIntoDao(source)).thenReturn(result)
                whenever(dao.insert(result.data!!)).thenReturn(DataFactory.randomLong())
                whenever(gamerepo.save(any())).thenReturn(Completable.complete())
                whenever(platformrepo.save(any())).thenReturn(Completable.complete())
                observer = repository.save(source).test()
            }

            @Test
            @DisplayName("Then should request GameDatasourceRepository to save game")
            fun shouldRequestGamesDao() {
                verify(gamerepo).save(source.game!!)
            }

            @Test
            @DisplayName("Then should request PlatformDatasourceRepository to save platform")
            fun shouldRequestPlatformsDao() {
                verify(platformrepo).save(source.platform!!)
            }

            @Test
            @DisplayName("Then should request RelationDaoService to save relation")
            fun shouldRequestDao() {
                verify(dao).insert(result.data!!)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertNoValues()
                    assertComplete()
                }
            }

            @Nested
            @DisplayName("And GameDatasourceRepository fails to save")
            inner class GameDatasourceRepositoryFails {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    reset(dao)
                    whenever(gamerepo.save(any())).thenReturn(Completable.error(error))
                    observer = repository.save(source).test()
                }

                @Test
                @DisplayName("Then should not request RelationDaoService to save")
                fun shouldNotRequestDao() = verifyZeroInteractions(dao)

                @Test
                @DisplayName("Then should emit error")
                fun shouldEmitError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError(error)
                    }
                }
            }

            @Nested
            @DisplayName("And PlatformDatasourceRepository fails to save")
            inner class PlatformDatasourceRepositoryFails {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    reset(platformrepo)
                    reset(dao)
                    whenever(platformrepo.save(any())).thenReturn(Completable.error(error))
                    observer = repository.save(source).test()
                }

                @Test
                @DisplayName("Then should not request RelationDaoService to save")
                fun shouldNotRequestDao() {
                    verifyZeroInteractions(dao)
                }

                @Test
                @DisplayName("Then should emit error")
                fun shouldEmitError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError(error)
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call loadForGameAndPlatform")
        inner class LoadForGameAndPlatformCalled {

            private var observer: TestSubscriber<List<GameRelation>>? = null
            private val daomodel1 = makeGameRelationWithGameAndPlatform()
            private val entity1 = makeGameRelation()
            private val gameId = 10
            private val platformId = 5

            @BeforeEach
            internal fun setUp() {
                val flowable = Flowable.create<List<RelationWithGameAndPlatform>>({
                    it.onNext(listOf(daomodel1))
                    it.onNext(listOf(daomodel1))
                }, BackpressureStrategy.MISSING)
                whenever(mapper.mapFromDao(daomodel1)).thenReturn(entity1)
                whenever(dao.loadForGameAndPlatform(gameId, platformId)).thenReturn(flowable)
                observer = repository.loadForGameAndPlatform(gameId, platformId).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).loadForGameAndPlatform(gameId, platformId)
            }

            @Test
            @DisplayName("Then should map result")
            fun shouldMapResults() {
                verify(mapper).mapFromDao(daomodel1)
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
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class LoadForGameCalled {

            private var observer: TestSubscriber<List<GameRelation>>? = null
            private val gameId = 100
            private val emission1 = listOf(makeGameRelationWithGameAndPlatform(), makeGameRelationWithGameAndPlatform())
            private val emission2 = listOf(makeGameRelationWithGameAndPlatform(), makeGameRelationWithGameAndPlatform(), makeGameRelationWithGameAndPlatform())

            @BeforeEach
            internal fun setUp() {
                val flowable = Flowable.create<List<RelationWithGameAndPlatform>>({
                    it.onNext(emission1)
                    it.onNext(emission2)
                }, BackpressureStrategy.MISSING)
                whenever(dao.load(anyInt())).thenReturn(flowable)

                whenever(mapper.mapFromDao(any())).thenReturn(makeGameRelation())
                observer = repository.loadForGame(gameId).test()
            }

            @Test
            @DisplayName("Then should request dao")
            fun shouldRequestDao() {
                verify(dao).load(gameId)
            }

            @Test
            @DisplayName("Then should map result")
            fun shouldMapResults() {
                verify(mapper, times(emission1.size + emission2.size)).mapFromDao(any())
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