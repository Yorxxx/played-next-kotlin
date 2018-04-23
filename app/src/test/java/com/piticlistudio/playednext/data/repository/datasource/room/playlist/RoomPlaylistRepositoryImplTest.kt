package com.piticlistudio.playednext.data.repository.datasource.room.playlist

import android.database.sqlite.SQLiteConstraintException
import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.room.RoomPlaylistEntity
import com.piticlistudio.playednext.data.entity.room.RoomPlaylistGameRelationEntity
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.gameplaylist.RoomGamePlaylistService
import com.piticlistudio.playednext.domain.model.Playlist
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomString
import com.piticlistudio.playednext.factory.PlaylistFactory.Factory.makePlaylist
import com.piticlistudio.playednext.factory.PlaylistFactory.Factory.makeRoomPlaylist
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class RoomPlaylistRepositoryImplTest {

    @Nested
    @DisplayName("Given RoomPlaylistRepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: RoomPlaylistRepositoryImpl
        private val dao: RoomPlaylistService = mock()
        private val mapper: DomainLayerMapper<Playlist, RoomPlaylistEntity> = mock()
        private val dataLayerMapper: DataLayerMapper<RoomPlaylistEntity, Playlist> = mock()
        private val roomGameRepositoryImpl: RoomGameRepositoryImpl = mock()
        private val relationsDao: RoomGamePlaylistService = mock()

        @BeforeEach
        internal fun setUp() {
            repository = RoomPlaylistRepositoryImpl(dao, relationsDao, mapper, dataLayerMapper, roomGameRepositoryImpl)
        }

        @Nested
        @DisplayName("When we call save")
        inner class SaveCalled {

            val playlist = makePlaylist()
            val mappedPlaylist = makeRoomPlaylist()
            private var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapIntoDataLayerModel(playlist)).thenReturn(mappedPlaylist)
                observer = repository.save(playlist).test()
            }

            @Test
            @DisplayName("Then should request mapper to map playlist")
            fun shouldMap() {
                verify(mapper).mapIntoDataLayerModel(playlist)
            }

            @Test
            @DisplayName("Then should request dao to insert")
            fun shouldInsert() {
                verify(dao).insert(mappedPlaylist)
            }

            @Test
            @DisplayName("then should request to save every relation")
            fun requestsGameSaving() {

                val captor = argumentCaptor<RoomPlaylistGameRelationEntity>()
                verify(relationsDao, times(playlist.games.size)).insert(captor.capture())

                captor.allValues.forEachIndexed { index, entity ->
                    assertEquals(playlist.name, entity.playlistName)
                    assertEquals(playlist.games[index].id, entity.gameId)
                }
            }


            @Nested
            @DisplayName("and saving the game-relation fails")
            inner class RelationSavingFailed {

                @BeforeEach
                internal fun setUp() {
                    whenever(relationsDao.insert(any())).thenThrow(SQLiteConstraintException())
                    observer = repository.save(playlist).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun etmisError() {
                    with(observer!!) {
                        assertNoValues()
                        assertNotComplete()
                        assertError { it is SQLiteConstraintException }
                    }
                }
            }

            @Nested
            @DisplayName("and saving the game-relation")
            inner class RelationSavingSuccess {

                @BeforeEach
                internal fun setUp() {
                    whenever(relationsDao.insert(any())).thenReturn(randomLong())
                    observer = repository.save(playlist).test()
                }

                @Test
                @DisplayName("Then should emit completion")
                fun emits() {
                    with(observer!!) {
                        assertNoErrors()
                        assertNoValues()
                        assertComplete()
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call delete")
        inner class DeleteCalled {

            val playlist = makePlaylist()
            val mappedPlaylist = makeRoomPlaylist()
            private var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapIntoDataLayerModel(playlist)).thenReturn(mappedPlaylist)
                observer = repository.delete(playlist).test()
            }

            @Test
            @DisplayName("Then should request mapper to map playlist")
            fun shouldMap() {
                verify(mapper).mapIntoDataLayerModel(playlist)
            }

            @Test
            @DisplayName("Then should request dao to delete")
            fun shouldInsert() {
                verify(dao).delete(mappedPlaylist)
            }


            @Test
            @DisplayName("Then should emit completion")
            fun emitsComplete() {
                with(observer!!) {
                    assertNoErrors()
                    assertNoValues()
                    assertComplete()
                }
            }
        }

        @Nested
        @DisplayName("When we call find playlist")
        inner class FindCalled {

            private val request = "foo"
            private var observer: TestSubscriber<Playlist>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(dao.find(any())).thenReturn(Flowable.just(listOf(makeRoomPlaylist())))
                observer = repository.find(request).test()
            }

            @Test
            @DisplayName("Then should request dao")
            fun requestsDao() {
                verify(dao).find(request)
            }

            @Nested
            @DisplayName("And dao succeeds")
            inner class FindSuccess {

                val daoData = makeRoomPlaylist()
                val mappedData = makePlaylist(games = listOf())
                val daoRelations = randomListOf(randomInt(20)){  RoomPlaylistGameRelationEntity(randomString(), randomInt()) }

                @BeforeEach
                internal fun setUp() {
                    val flowable = Flowable.create<List<RoomPlaylistEntity>>({
                        it.onNext(listOf(daoData))
                    }, BackpressureStrategy.MISSING)
                    whenever(dataLayerMapper.mapFromDataLayer(any())).thenReturn(mappedData)
                    whenever(dao.find(any())).thenReturn(flowable)
                    whenever(relationsDao.find(any())).thenReturn(Flowable.just(daoRelations))
                    whenever(roomGameRepositoryImpl.load(any())).thenReturn(Flowable.just(makeGame()))
                    observer = repository.find(request).test()
                }

                @Test
                @DisplayName("Then should map response")
                fun shouldMap() {
                    verify(dataLayerMapper).mapFromDataLayer(daoData)
                }

                @Test
                @DisplayName("Then should request to load all relations for that playlist")
                fun requestsRelations() {
                    verify(relationsDao).find(mappedData.name)
                }

                @Test
                @DisplayName("then should request to load every game from that playlist")
                fun requestsGame() {
                    verify(roomGameRepositoryImpl, times(daoRelations.size)).load(any())
                    daoRelations.forEach {
                        verify(roomGameRepositoryImpl).load(it.gameId)
                    }
                }

                @Test
                @DisplayName("Then should emit playlist with games")
                fun playlistHasGames() {
                    with(observer!!) {
                        assertValueAt(0, {
                            it.games.size == daoRelations.size
                        })
                    }
                }

                @Test
                @DisplayName("Then should emit success")
                fun shouldEmit() {
                    with(observer!!) {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                    }
                }
            }
        }


        @Nested
        @DisplayName("When we call findAll")
        inner class FindAll {

            val playlists = randomListOf(randomInt(20)){ makeRoomPlaylist() }
            private var observer: TestSubscriber<List<Playlist>>? = null

            @BeforeEach
            internal fun setUp() {
                val flowable = Flowable.create<List<RoomPlaylistEntity>>({
                    it.onNext(playlists)
                }, BackpressureStrategy.MISSING)
                whenever(dao.findAll()).thenReturn(flowable)
                observer = repository.findAll().test()
            }

            @Test
            @DisplayName("then should request to load all playlists")
            fun requestsDao() {
                verify(dao).findAll()
            }

            @Nested
            @DisplayName("And dao succeeds")
            inner class FindSuccess {

                @BeforeEach
                internal fun setUp() {
                    reset(dataLayerMapper, relationsDao)
                    doAnswer {
                        val arg = it.arguments[0] as RoomPlaylistEntity
                        return@doAnswer makePlaylist(arg.name)
                    }.whenever(dataLayerMapper).mapFromDataLayer(any())

                    doAnswer {
                        val arg = it.arguments[0] as String
                        val relations = mutableListOf<RoomPlaylistGameRelationEntity>()
                        repeat(15) {
                            relations.add(RoomPlaylistGameRelationEntity(arg, randomInt()))
                        }
                        return@doAnswer Flowable.just(relations)
                    }.whenever(relationsDao).find(any())

                    doAnswer {
                        val arg = it.arguments[0] as Int
                        return@doAnswer Flowable.just(makeGame(arg))
                    }.whenever(roomGameRepositoryImpl).load(any())
                    observer = repository.findAll().test()
                }

                @Test
                @DisplayName("Then should map every playlist")
                fun shouldMap() {
                    verify(dataLayerMapper, times(playlists.size)).mapFromDataLayer(any())
                    playlists.forEach {
                        verify(dataLayerMapper).mapFromDataLayer(it)
                    }
                }

                @Test
                @DisplayName("Then should request to load relations for each playlist")
                fun requestsRelations() {
                    verify(relationsDao, times(playlists.size)).find(any())
                    playlists.forEach {
                        verify(relationsDao).find(it.name)
                    }
                }

                @Test
                @DisplayName("then should request to load every game from every relation for each playlist")
                fun requestsGame() {
                    verify(roomGameRepositoryImpl, atLeast(playlists.size * 15)).load(any())
                }

                @Test
                @DisplayName("Then should emit list of playlists with games")
                fun playlistHasGames() {
                    with(observer!!) {
                        assertValueAt(0, {
                            it.size == playlists.size
                        })
                    }
                }

                @Test
                @DisplayName("Then should emit success")
                fun shouldEmit() {
                    with(observer!!) {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                    }
                }
            }
        }
    }
}