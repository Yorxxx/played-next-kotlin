package com.piticlistudio.playednext.data.repository.datasource.room.relation

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.datasources.relation.RoomRelationMapper
import com.piticlistudio.playednext.data.entity.room.RoomGameRelation
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.platform.RoomGamePlatformRepositoryImpl
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.test.factory.DataFactory
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeRoomGameRelationProxy
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
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

internal class RoomRelationRepositoryImplTest {

    @Nested
    @DisplayName("Given a RelationDaoRepositoryImpl instance")
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
                observer = repository.save(source).test()
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
                fun withoutErrors() {
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
    }
}