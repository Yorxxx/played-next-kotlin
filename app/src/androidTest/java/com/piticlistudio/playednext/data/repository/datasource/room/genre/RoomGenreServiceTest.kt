package com.piticlistudio.playednext.data.repository.datasource.room.genre

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.room.RoomGameGenre
import com.piticlistudio.playednext.factory.DomainFactory
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class RoomGenreServiceTest {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var database: AppDatabase? = null

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @Test
    fun insertShouldStoreData() {

        val data = DomainFactory.makeRoomGenre()

        val result = database?.genreRoom()?.insert(data)

        Assert.assertNotNull(result)
        Assert.assertEquals(data.id, result!!.toInt())
    }

    fun insertShouldIgnoreIfAlreadyStored() {

        val data = DomainFactory.makeRoomGenre()

        val id = database?.genreRoom()?.insert(data)
        val id2 = database?.genreRoom()?.insert(data)

        Assert.assertTrue(id!! > 0L)
        Assert.assertEquals(0L, id2)
    }

    @Test
    fun insertGameGenreShouldStoreData() {
        val genre = DomainFactory.makeRoomGenre()
        val game = DomainFactory.makeGameCache()
        val data = RoomGameGenre(game.id, genre.id)

        database?.gamesDao()?.insert(game)
        database?.genreRoom()?.insert(genre)
        val result = database?.genreRoom()?.insertGameGenre(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun insertGameDeveloperShouldReplaceDataOnConflict() {
        val genre = DomainFactory.makeRoomGenre()
        val game = DomainFactory.makeGameCache()
        val data = RoomGameGenre(game.id, genre.id)

        database?.gamesDao()?.insert(game)
        database?.genreRoom()?.insert(genre)
        database?.genreRoom()?.insertGameGenre(data)
        val result = database?.genreRoom()?.insertGameGenre(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun findForGameShouldReturnData() {
        val game = DomainFactory.makeGameCache()
        val game2 = DomainFactory.makeGameCache()
        val genre1 = DomainFactory.makeRoomGenre()
        val genre2 = DomainFactory.makeRoomGenre()
        val data = RoomGameGenre(game.id, genre1.id)
        val data2 = RoomGameGenre(game.id, genre2.id)
        val data3 = RoomGameGenre(game2.id, genre1.id)

        database?.gamesDao()?.insert(game)
        database?.gamesDao()?.insert(game2)
        database?.genreRoom()?.insert(genre1)
        database?.genreRoom()?.insert(genre2)
        database?.genreRoom()?.insertGameGenre(data)
        database?.genreRoom()?.insertGameGenre(data2)
        database?.genreRoom()?.insertGameGenre(data3)

        // Act
        val observer = database?.genreRoom()?.findForGame(game.id)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue {
                it.size == 2 && it.contains(genre1) && it.contains(genre2)
            }
        }
    }

    @Test
    fun findForGameShouldReturnEmptyListWhenNoMatches() {

        val game = DomainFactory.makeGameCache()
        database?.gamesDao()?.insert(game)

        // Act
        // Act
        val observer = database?.genreRoom()?.findForGame(game.id)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue { it.isEmpty() }
        }
    }

    @After
    fun tearDown() {
        database?.close()
    }
}