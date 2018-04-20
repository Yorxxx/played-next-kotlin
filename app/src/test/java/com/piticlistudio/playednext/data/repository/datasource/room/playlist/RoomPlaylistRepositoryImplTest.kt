package com.piticlistudio.playednext.data.repository.datasource.room.playlist

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.room.RoomGameRelation
import com.piticlistudio.playednext.data.entity.room.RoomPlaylist
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameRepositoryImpl
import com.piticlistudio.playednext.domain.model.Playlist
import com.piticlistudio.playednext.factory.PlaylistFactory.Factory.makePlaylist
import com.piticlistudio.playednext.factory.PlaylistFactory.Factory.makeRoomPlaylist
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RoomPlaylistRepositoryImplTest {

    @Nested
    @DisplayName("Given RoomPlaylistRepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: RoomPlaylistRepositoryImpl
        private val dao: RoomPlaylistService = mock()
        private val mapper: DomainLayerMapper<Playlist, RoomPlaylist> = mock()
        private val dataLayerMapper: DataLayerMapper<RoomPlaylist, Playlist> = mock()
        private val roomGameRepositoryImpl: RoomGameRepositoryImpl = mock()

        @BeforeEach
        internal fun setUp() {
            repository = RoomPlaylistRepositoryImpl(dao, mapper, dataLayerMapper, roomGameRepositoryImpl)
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

            @Nested
            @DisplayName("and insert dao call succeeds")
            inner class InsertSuccess {

                @BeforeEach
                internal fun setUp() {
                    whenever(dao.insert(any())).thenReturn(10L)
                    observer = repository.save(playlist).test()
                }

                @Test
                @DisplayName("Then should emit completion")
                fun emitsComplete() {
                    with(observer!!){
                        assertNoErrors()
                        assertNoValues()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and insert dao call fails")
            inner class InsertFailure {

                @BeforeEach
                internal fun setUp() {
                    whenever(dao.insert(any())).thenReturn(0L)
                    observer = repository.save(playlist).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsComplete() {
                    with(observer!!){
                        assertNoValues()
                        assertNotComplete()
                        assertError { it is PlaylistRepositoryError.Save }
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

            @Nested
            @DisplayName("and delete request succeeds")
            inner class DaoSuccess {

                @BeforeEach
                internal fun setUp() {
                    whenever(dao.delete(any())).thenReturn(10)
                    observer = repository.delete(playlist).test()
                }

                @Test
                @DisplayName("Then should emit completion")
                fun emitsComplete() {
                    with(observer!!){
                        assertNoErrors()
                        assertNoValues()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and delete request fails")
            inner class DaoFailure {

                @BeforeEach
                internal fun setUp() {
                    whenever(dao.delete(any())).thenReturn(0)
                    observer = repository.delete(playlist).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsComplete() {
                    with(observer!!){
                        assertNoValues()
                        assertNotComplete()
                        assertError { it is PlaylistRepositoryError.Delete }
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call find")
        inner class FindCalled {

            private val request = "foo"
            private var observer: TestSubscriber<Playlist>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(dao.find(any())).thenReturn(Flowable.just(makeRoomPlaylist()))
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
                val mappedData = makePlaylist()

                @BeforeEach
                internal fun setUp() {
                    val flowable = Flowable.create<RoomPlaylist>({
                        it.onNext(daoData)
                    }, BackpressureStrategy.MISSING)
                    whenever(dataLayerMapper.mapFromDataLayer(any())).thenReturn(mappedData)
                    whenever(dao.find(any())).thenReturn(flowable)
                    observer = repository.find(request).test()
                }

                @Test
                @DisplayName("Then should map response")
                fun shouldMap() {
                    verify(dataLayerMapper).mapFromDataLayer(daoData)
                }

                @Test
                @DisplayName("Then should emit mapped response")
                fun shouldEmit() {
                    with(observer!!) {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue(mappedData)
                    }
                }
            }
        }
    }
}