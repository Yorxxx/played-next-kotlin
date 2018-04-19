package com.piticlistudio.playednext.data.repository.datasource.room

import android.arch.persistence.room.EmptyResultSetException
import android.database.sqlite.SQLiteConstraintException
import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.mapper.datasources.game.RoomGameMapper
import com.piticlistudio.playednext.data.entity.room.RoomGame
import com.piticlistudio.playednext.data.entity.room.RoomGameProxy
import com.piticlistudio.playednext.data.repository.datasource.room.company.RoomCompanyRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.franchise.RoomCollectionRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.game.GameSaveException
import com.piticlistudio.playednext.data.repository.datasource.room.game.GameUpdateException
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameService
import com.piticlistudio.playednext.data.repository.datasource.room.genre.RoomGenreRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.image.RoomGameImageRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.platform.RoomGamePlatformRepositoryImpl
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeRoomGame
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*

internal class RoomGameRepositoryImplTest {

    @Nested
    @DisplayName("Given RoomGameRepositoryImpl instance")
    inner class LocalImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        private val daoService: RoomGameService = mock()
        private val mapper: RoomGameMapper = mock()
        private val companyRepositoryImpl: RoomCompanyRepositoryImpl = mock()
        private val imagesRepositoryImpl: RoomGameImageRepositoryImpl = mock()
        private val collectionRepositoryImpl: RoomCollectionRepositoryImpl = mock()
        private val genreRepositoryImpl: RoomGenreRepositoryImpl = mock()
        private val platformRepositoryImpl: RoomGamePlatformRepositoryImpl = mock()

        private lateinit var repository: RoomGameRepositoryImpl

        @BeforeEach
        internal fun setUp() {
            reset(daoService, mapper, companyRepositoryImpl, imagesRepositoryImpl, collectionRepositoryImpl, genreRepositoryImpl, platformRepositoryImpl)
            repository = RoomGameRepositoryImpl(daoService, companyRepositoryImpl, imagesRepositoryImpl, collectionRepositoryImpl, genreRepositoryImpl, platformRepositoryImpl, mapper)
        }

        @Nested
        @DisplayName("When load is called")
        inner class LoadIsCalled {

            private var observer: TestSubscriber<Game>? = null
            private val model = makeRoomGame()
            private val entity = makeGame()
            private val devs = entity.developers
            private val pubs = entity.publishers
            private val gens = entity.genres
            private val col = entity.collection
            private val screenshots = entity.images

            @BeforeEach
            internal fun setUp() {
                val flowable = Flowable.create<List<RoomGame>>({
                    it.onNext(listOf(model))
                    it.onNext(listOf(model))
                }, BackpressureStrategy.MISSING)
                whenever(daoService.findById(10)).thenReturn(flowable)
                whenever(mapper.mapFromDataLayer(anyOrNull())).thenReturn(entity)

                whenever(companyRepositoryImpl.loadDevelopersForGame(anyInt())).thenReturn(Flowable.just(devs))
                whenever(companyRepositoryImpl.loadPublishersForGame(anyInt())).thenReturn(Flowable.just(pubs))
                whenever(imagesRepositoryImpl.loadForGame(anyInt())).thenReturn(Flowable.just(screenshots))
                whenever(genreRepositoryImpl.loadForGame(anyInt())).thenReturn(Flowable.just(gens))
                whenever(collectionRepositoryImpl.loadForGame(anyInt())).thenReturn(Flowable.just(listOf(col!!)))
                whenever(platformRepositoryImpl.loadForGame(anyInt())).thenReturn(Flowable.just(entity.platforms))
                observer = repository.load(10).test()
            }

            @Test
            @DisplayName("Then should request DAO")
            fun daoIsCalled() {
                verify(daoService).findById(10)
            }

            @Test
            @DisplayName("Then should request developers for game")
            fun requestsDevelopers() {
                verify(companyRepositoryImpl).loadDevelopersForGame(10)
            }

            @Test
            @DisplayName("Then should request publishers for game")
            fun requestsPublishers() {
                verify(companyRepositoryImpl).loadPublishersForGame(10)
            }

            @Test
            @DisplayName("Then should request genres for game")
            fun requestsGenres() {
                verify(genreRepositoryImpl).loadForGame(10)
            }

            @Test
            @DisplayName("Then should request images for game")
            fun requestsImages() {
                verify(imagesRepositoryImpl).loadForGame(10)
            }

            @Test
            @DisplayName("Then should request collection for game")
            fun requestsCollection() {
                verify(collectionRepositoryImpl).loadForGame(10)
            }

            @Test
            @DisplayName("Then should request platforms for game")
            fun requestsPlatforms() {
                verify(platformRepositoryImpl).loadForGame(10)
            }

            @Test
            @DisplayName("Then should map daoService result")
            fun mapIsCalled() {
                verify(mapper).mapFromDataLayer(argForWhich {
                    this.game == model && this.collection == col
                            && this.developers == devs
                            && publishers == pubs
                            && images == screenshots
                            && genres == gens
                            && platforms == entity.platforms
                })
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
                }
            }

            @Nested
            @DisplayName("And images repository fails to load")
            inner class ImageError {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(imagesRepositoryImpl.loadForGame(anyInt())).thenReturn(Flowable.error(error))
                    observer = repository.load(10).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withoutErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError(error)
                    }
                }
            }

            @Nested
            @DisplayName("And developers repository fails to load")
            inner class DevelopersError {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(companyRepositoryImpl.loadDevelopersForGame(anyInt())).thenReturn(Flowable.error(error))
                    observer = repository.load(10).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withoutErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError(error)
                    }
                }
            }

            @Nested
            @DisplayName("And publishers repository fails to load")
            inner class PublishersError {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(companyRepositoryImpl.loadPublishersForGame(anyInt())).thenReturn(Flowable.error(error))
                    observer = repository.load(10).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withoutErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError(error)
                    }
                }
            }

            @Nested
            @DisplayName("And collection repository fails to load")
            inner class CollectionError {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(collectionRepositoryImpl.loadForGame(anyInt())).thenReturn(Flowable.error(error))
                    observer = repository.load(10).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withoutErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError(error)
                    }
                }
            }

            @Nested
            @DisplayName("And collection repository emits empty list")
            inner class CollectionEmpty {

                @BeforeEach
                internal fun setUp() {
                    whenever(collectionRepositoryImpl.loadForGame(anyInt())).thenReturn(Flowable.just(listOf()))
                    observer = repository.load(10).test()
                }

                @Test
                @DisplayName("Then should emit without collection")
                fun withoutErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNotComplete()
                        assertValueCount(1)
                        assertNoErrors()
                        assertValue(entity)
                    }
                }
            }

            @Nested
            @DisplayName("And genre repository fails to load")
            inner class GenreError {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(genreRepositoryImpl.loadForGame(anyInt())).thenReturn(Flowable.error(error))
                    observer = repository.load(10).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withoutErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError(error)
                    }
                }
            }

            @Nested
            @DisplayName("And platform repository fails to load")
            inner class PlatformError {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(platformRepositoryImpl.loadForGame(anyInt())).thenReturn(Flowable.error(error))
                    observer = repository.load(10).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withoutErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError(error)
                    }
                }
            }

            @Nested
            @DisplayName("And there are no results in database")
            inner class NoResults {

                @BeforeEach
                internal fun setUp() {
                    reset(companyRepositoryImpl, genreRepositoryImpl, imagesRepositoryImpl, collectionRepositoryImpl, platformRepositoryImpl)
                    val flowable = Flowable.create<List<RoomGame>>({ it.onNext(listOf()) }, BackpressureStrategy.MISSING)
                    whenever(daoService.findById(anyLong())).thenReturn(flowable)
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
                    verifyZeroInteractions(companyRepositoryImpl)
                    verifyZeroInteractions(genreRepositoryImpl)
                    verifyZeroInteractions(imagesRepositoryImpl)
                    verifyZeroInteractions(collectionRepositoryImpl)
                    verifyZeroInteractions(platformRepositoryImpl)
                }

            }

            @Nested
            @DisplayName("And GamesDao emits error")
            inner class GameRoomError {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    reset(companyRepositoryImpl, genreRepositoryImpl, imagesRepositoryImpl, collectionRepositoryImpl, platformRepositoryImpl)
                    val flowable = Flowable.create<List<RoomGame>>({ it.onError(error) }, BackpressureStrategy.MISSING)
                    whenever(daoService.findById(anyLong())).thenReturn(flowable)
                    observer = repository.load(10).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withoutErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError(error)
                    }
                    verifyZeroInteractions(companyRepositoryImpl)
                    verifyZeroInteractions(genreRepositoryImpl)
                    verifyZeroInteractions(imagesRepositoryImpl)
                    verifyZeroInteractions(collectionRepositoryImpl)
                    verifyZeroInteractions(platformRepositoryImpl)
                }

            }
        }

        @Nested
        @DisplayName("When save is called")
        inner class SaveIsCalled {

            private val source = makeGame()
            private val data = RoomGameProxy(makeRoomGame())
            private var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapIntoDataLayerModel(source)).thenReturn(data)
                whenever(companyRepositoryImpl.saveDeveloperForGame(anyInt(), anyOrNull())).thenReturn(Completable.complete())
                whenever(companyRepositoryImpl.savePublisherForGame(anyInt(), anyOrNull())).thenReturn(Completable.complete())
                whenever(imagesRepositoryImpl.saveForGame(anyOrNull())).thenReturn(Completable.complete())
                whenever(collectionRepositoryImpl.saveForGame(anyInt(), anyOrNull())).thenReturn(Completable.complete())
                whenever(platformRepositoryImpl.saveForGame(anyInt(), anyOrNull())).thenReturn(Completable.complete())
                whenever(genreRepositoryImpl.saveGenreForGame(anyInt(), anyOrNull())).thenReturn(Completable.complete())
                observer = repository.save(source).test()
            }

            @Test
            @DisplayName("Then maps entity into Dao model")
            fun mapsEntity() {
                verify(mapper).mapIntoDataLayerModel(source)
            }

            @Test
            @DisplayName("Then requests dao to insert")
            fun requestsInsert() {
                verify(daoService).insert(data.game)
            }

            @Nested
            @DisplayName("And insert succeeds")
            inner class InsertSuccess {

                @BeforeEach
                internal fun setUp() {
                    whenever(daoService.insert(anyOrNull())).thenReturn(10L)
                    observer = repository.save(source).test()
                }

                @Test
                @DisplayName("Then requests to save developers")
                fun savesDevelopers() {
                    source.developers.forEach {
                        verify(companyRepositoryImpl).saveDeveloperForGame(source.id, it)
                    }
                }

                @Test
                @DisplayName("Then requests to save publishers")
                fun savesPublishers() {
                    source.publishers.forEach {
                        verify(companyRepositoryImpl).savePublisherForGame(source.id, it)
                    }
                }

                @Test
                @DisplayName("Then requests to save genres")
                fun savesGenres() {
                    source.genres.forEach {
                        verify(genreRepositoryImpl).saveGenreForGame(source.id, it)
                    }
                }

                @Test
                @DisplayName("Then requests to save platforms")
                fun savesPlatforms() {
                    source.platforms.forEach {
                        verify(platformRepositoryImpl).saveForGame(source.id, it)
                    }
                }

                @Test
                @DisplayName("Then requests to save images")
                fun savesImages() {
                    source.images.forEach {
                        verify(imagesRepositoryImpl).saveForGame(it)
                    }
                }

                @Test
                @DisplayName("Then requests to save collection")
                fun savesCollection() {
                    source.collection?.let {
                        verify(collectionRepositoryImpl).saveForGame(source.id, it)
                    }
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
                @DisplayName("And game does not have collection")
                inner class WithoutCollection {

                    private val source = makeGame(collection = null)

                    @BeforeEach
                    internal fun setUp() {
                        reset(collectionRepositoryImpl)
                        whenever(mapper.mapIntoDataLayerModel(source)).thenReturn(data)
                        observer = repository.save(source).test()
                    }

                    @Test
                    @DisplayName("Then emits completion")
                    fun emitsError() {

                        assertNotNull(observer)
                        observer?.apply {
                            assertNoValues()
                            assertNoErrors()
                            assertComplete()
                        }
                    }

                    @Test
                    @DisplayName("Then skips collection saving")
                    fun savesDevelopers() {
                        verifyZeroInteractions(collectionRepositoryImpl)
                    }
                }
            }

            @Nested
            @DisplayName("And insert fails because is already stored")
            inner class AlreadyStored {

                @BeforeEach
                internal fun setUp() {
                    whenever(daoService.insert(anyOrNull())).thenThrow(SQLiteConstraintException())
                    observer = repository.save(source).test()
                }

                @Test
                @DisplayName("Then tries to update")
                fun triesToUpdate() {
                    verify(daoService).update(data.game)
                }

                @Nested
                @DisplayName("And update succeeds")
                inner class UpdateSuccess {

                    @BeforeEach
                    internal fun setUp() {
                        whenever(daoService.update(anyOrNull())).thenReturn(10)
                        observer = repository.save(source).test()
                    }

                    @Test
                    @DisplayName("Then requests to save developers")
                    fun savesDevelopers() {
                        source.developers.forEach {
                            verify(companyRepositoryImpl).saveDeveloperForGame(source.id, it)
                        }
                    }

                    @Test
                    @DisplayName("Then requests to save publishers")
                    fun savesPublishers() {
                        source.publishers.forEach {
                            verify(companyRepositoryImpl).savePublisherForGame(source.id, it)
                        }
                    }

                    @Test
                    @DisplayName("Then requests to save genres")
                    fun savesGenres() {
                        source.genres.forEach {
                            verify(genreRepositoryImpl).saveGenreForGame(source.id, it)
                        }
                    }

                    @Test
                    @DisplayName("Then requests to save platforms")
                    fun savesPlatforms() {
                        source.platforms.forEach {
                            verify(platformRepositoryImpl).saveForGame(source.id, it)
                        }
                    }

                    @Test
                    @DisplayName("Then requests to save images")
                    fun savesImages() {
                        source.images.forEach {
                            verify(imagesRepositoryImpl).saveForGame(it)
                        }
                    }

                    @Test
                    @DisplayName("Then requests to save collection")
                    fun savesCollection() {
                        source.collection?.let {
                            verify(collectionRepositoryImpl).saveForGame(source.id, it)
                        }
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

                @Nested
                @DisplayName("And update fails")
                inner class UpdateFailed {

                    @BeforeEach
                    internal fun setUp() {
                        whenever(daoService.update(anyOrNull())).thenReturn(0)
                        observer = repository.save(source).test()
                    }

                    @Test
                    @DisplayName("Then emits error")
                    fun emitsError() {
                        assertNotNull(observer)
                        observer?.apply {
                            assertError(GameUpdateException::class.java)
                            assertNoValues()
                        }
                    }

                }
            }

            @Nested
            @DisplayName("And inserts fails to save")
            inner class SaveFailed {

                @BeforeEach
                internal fun setUp() {
                    reset(daoService)
                    whenever(daoService.insert(anyOrNull())).thenReturn(0L)
                    observer = repository.save(source).test()
                }

                @Test
                @DisplayName("Then emits error")
                fun emitsError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertError(GameSaveException::class.java)
                        assertNotComplete()
                    }
                }

                @Test
                @DisplayName("Then does not save developers")
                fun savesDevelopers() {
                    source.developers.forEach {
                        verify(companyRepositoryImpl, never()).saveDeveloperForGame(source.id, it)
                    }
                }

                @Test
                @DisplayName("Then does not saves publishers")
                fun savesPublishers() {
                    source.publishers.forEach {
                        verify(companyRepositoryImpl, never()).savePublisherForGame(source.id, it)
                    }
                }

                @Test
                @DisplayName("Then does not saves genres")
                fun savesGenres() {
                    source.genres.forEach {
                        verify(genreRepositoryImpl, never()).saveGenreForGame(source.id, it)
                    }
                }

                @Test
                @DisplayName("Then does not save platforms")
                fun savesPlatforms() {
                    source.platforms.forEach {
                        verify(platformRepositoryImpl, never()).saveForGame(source.id, it)
                    }
                }

                @Test
                @DisplayName("Then does not save images")
                fun savesImages() {
                    source.images.forEach {
                        verify(imagesRepositoryImpl, never()).saveForGame(it)
                    }
                }

                @Test
                @DisplayName("Then does not saves collection")
                fun savesCollection() {
                    source.collection?.let {
                        verify(collectionRepositoryImpl, never()).saveForGame(source.id, it)
                    }
                }
            }
        }

        @Nested
        @DisplayName("When search is called")
        inner class SearchCalled {

            private val data1 = makeRoomGame()
            private val data2 = makeRoomGame()
            private val data3 = makeRoomGame()
            private var observer: TestSubscriber<List<Game>>? = null
            private val entity1 = makeGame()

            @BeforeEach
            internal fun setUp() {
                val flowable = Flowable.create<List<RoomGame>>({
                    it.onNext(listOf(data1, data2))
                    it.onNext(listOf(data1, data2))
                    it.onNext(listOf(data1, data2, data3))
                }, BackpressureStrategy.MISSING)
                whenever(daoService.findByName(anyString())).thenReturn(flowable)
                whenever(mapper.mapFromDataLayer(anyOrNull())).thenReturn(entity1)
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
                verify(mapper, times(2)).mapFromDataLayer(argForWhich {
                    this.game == data1
                })
                verify(mapper, times(2)).mapFromDataLayer(argForWhich {
                    this.game == data2
                })
                verify(mapper).mapFromDataLayer(argForWhich {
                    this.game == data3
                })
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertValueCount(2)
                    assertNotComplete()
                    assertValueAt(0, { it.size == 2 })
                    assertValueAt(1, { it.size == 3 })
                }
            }
        }
    }
}