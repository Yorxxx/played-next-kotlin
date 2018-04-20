package com.piticlistudio.playednext.data.repository.datasource.room.relation

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.mapper.datasources.relation.RoomRelationMapper
import com.piticlistudio.playednext.data.entity.room.RoomGameRelation
import com.piticlistudio.playednext.data.repository.datasource.room.game.GameSaveException
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.platform.RoomGamePlatformRepositoryImpl
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.factory.DataFactory
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.factory.GameRelationFactory.Factory.makeRoomGameRelation
import com.piticlistudio.playednext.factory.GameRelationFactory.Factory.makeRoomGameRelationProxy
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.factory.PlatformFactory.Factory.makePlatform
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import kotlin.test.assertEquals

internal class RoomRelationRepositoryImplTest {

    @Nested
    @DisplayName("Given a RoomRelationRepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: RoomRelationRepositoryImpl
        val dao: RoomRelationService = mock()
        val mapper: RoomRelationMapper = mock()
        val gamesRepository: RoomGameRepositoryImpl = mock()
        val platformRepositoryImpl: RoomGamePlatformRepositoryImpl = mock()

        @BeforeEach
        internal fun setUp() {
            reset(dao, mapper, gamesRepository, platformRepositoryImpl)
            repository = RoomRelationRepositoryImpl(dao, gamesRepository, platformRepositoryImpl, mapper)
        }

        @Nested
        @DisplayName("When we call save")
        inner class SaveCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makeGameRelation()
            private val result = makeRoomGameRelationProxy()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapIntoDataLayerModel(source)).thenReturn(result)
                whenever(dao.insert(result.relation)).thenReturn(DataFactory.randomLong())
                whenever(gamesRepository.save(any())).thenReturn(Completable.complete())
                observer = repository.save(source).test()
            }

            @Test
            @DisplayName("Then should request to save Game")
            fun requestsGameSaving() {
                verify(gamesRepository).save(source.game)
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).insert(result.relation)
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
            @DisplayName("And gamerepository fails to insert")
            inner class GameSavingFailed {

                private val error = GameSaveException()

                @BeforeEach
                internal fun setUp() {
                    reset(dao)
                    whenever(gamesRepository.save(any())).thenReturn(Completable.error(error))
                    observer = repository.save(source).test()
                }

                @Test
                @DisplayName("Then does not save relation")
                fun noRelationSaving() {
                    verify(dao, never()).insert(result.relation)
                }

                @Test
                @DisplayName("Then should emit error")
                fun withErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertError { it is GameSaveException }
                        assertNotComplete()
                        assertNoValues()
                    }
                }
            }

            @Nested
            @DisplayName("And dao fails to insert")
            inner class InsertFailed {

                @BeforeEach
                internal fun setUp() {
                    reset(dao)
                    whenever(dao.insert(result.relation)).thenReturn(-1)
                    observer = repository.save(source).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertError { it is RelationSaveException }
                        assertNotComplete()
                        assertNoValues()
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call loadForGameAndPlatform")
        inner class LoadForGameAndPlatformCalled {

            private var observer: TestSubscriber<List<GameRelation>>? = null
            private val daomodel1 = makeRoomGameRelationProxy()
            private val game = makeGame()
            private val platform = makePlatform()
            private val gameId = 10
            private val platformId = 5

            @BeforeEach
            internal fun setUp() {
                val flowable = Flowable.create<List<RoomGameRelation>>({
                    it.onNext(listOf(daomodel1.relation))
                }, BackpressureStrategy.MISSING)
                whenever(dao.findForGameAndPlatform(gameId, platformId)).thenReturn(flowable)
                whenever(gamesRepository.load(anyInt())).thenReturn(Flowable.just(game))
                whenever(platformRepositoryImpl.load(anyInt())).thenReturn(Flowable.just(platform))
                observer = repository.loadForGameAndPlatform(gameId, platformId).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).findForGameAndPlatform(gameId, platformId)
            }

            @Test
            @DisplayName("Then should request game")
            fun shouldRequestGame() {
                verify(gamesRepository).load(gameId)
            }

            @Test
            @DisplayName("Then should request platform")
            fun shouldRequestPlatform() {
                verify(platformRepositoryImpl).load(platformId)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertNotComplete()
                    assertValueCount(1)
                    assertValue { it.size == 1 }
                    assertValue { it.first().game == game }
                    assertValue { it.first().platform == platform }
                    assertValue { it.first().updatedAt == daomodel1.relation.updated_at }
                    assertValue { it.first().createdAt == daomodel1.relation.created_at }
                }
            }
        }

        @Nested
        @DisplayName("When we call loadWithStatus")
        inner class LoadWithStatusCalled {

            private var observer: TestSubscriber<List<GameRelation>>? = null
            private val daoData = randomListOf(10) { makeRoomGameRelation() }
            private val status = GameRelationStatus.BEATEN
            private val daoEmissor = PublishSubject.create<List<RoomGameRelation>>()

            @BeforeEach
            internal fun setUp() {
                whenever(dao.findWithStatus(anyInt())).thenReturn(daoEmissor.toFlowable(BackpressureStrategy.MISSING))
                whenever(gamesRepository.load(anyInt())).thenReturn(Flowable.just(makeGame()))
                whenever(platformRepositoryImpl.load(anyInt())).thenReturn(Flowable.just(makePlatform()))
                observer = repository.loadWithStatus(status).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                daoEmissor.onNext(daoData)

                verify(dao).findWithStatus(status.ordinal)
            }

            @Test
            @DisplayName("Then should request game")
            fun shouldRequestGame() {
                daoEmissor.onNext(daoData)

                daoData.forEach {
                    verify(gamesRepository).load(it.gameId)
                }
            }

            @Test
            @DisplayName("Then should request platform")
            fun shouldRequestPlatform() {
                daoEmissor.onNext(daoData)

                daoData.forEach {
                    verify(platformRepositoryImpl).load(it.platformId)
                }
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                daoEmissor.onNext(daoData)

                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertNotComplete()
                    assertValueCount(1)
                    assertValue { it.size == daoData.size }
                    val emission = values().first()
                    assertEquals(daoData.size, emission.size)
                    emission.forEach {
                        assertNotNull(it.game)
                        assertNotNull(it.platform)
                    }
                }
            }

            @Nested
            @DisplayName("Should emit whenever dao changes")
            inner class MultipleEmissions {

                val daoData2 = randomListOf(12) { makeRoomGameRelation() }

                @BeforeEach
                internal fun setUp() {
                    daoEmissor.onNext(daoData)
                }

                @Test
                @DisplayName("Then should emit twice")
                fun emitsTwice() {

                    assertNotNull(observer)
                    observer?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue { it.size == daoData.size }
                    }

                    daoEmissor.onNext(daoData2)

                    assertNotNull(observer)
                    observer?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(2)
                        assertValueAt(0, { it.size == daoData.size })
                        assertValueAt(1, { it.size == daoData2.size })
                    }
                }
            }
        }
    }
}