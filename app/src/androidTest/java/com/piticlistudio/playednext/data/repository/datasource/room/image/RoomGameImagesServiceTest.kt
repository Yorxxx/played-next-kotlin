package com.piticlistudio.playednext.data.repository.datasource.room.image

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.database.sqlite.SQLiteConstraintException
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.factory.DomainFactory
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeRoomGameImage
import junit.framework.Assert
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomGameImagesServiceTest {

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

        val game = DomainFactory.makeGameCache()
        val data = makeRoomGameImage(gameId = game.id)

        database?.gamesDao()?.insert(game)
        val result = database?.imageRoom()?.insert(data)

        Assert.assertNotNull(result)
        assertEquals(data.id, result?.toInt())
    }

    @Test
    fun insertShouldAutoGenerateAPrimaryKey() {

        val game = DomainFactory.makeGameCache()
        val data = makeRoomGameImage(gameId = game.id, id = null)

        database?.gamesDao()?.insert(game)
        val result = database?.imageRoom()?.insert(data)

        Assert.assertNotNull(result)
        assertNotNull(result)
        assertEquals(1L, result)
    }

    @Test
    fun insertIsNotAllowedIfGameDoesNotExistOnDatabase() {

        val data = makeRoomGameImage()

        try {
            database?.imageRoom()?.insert(data)
        } catch (e: Throwable) {
            assertNotNull(e)
            assertTrue(e is SQLiteConstraintException)
        }
    }

    @Test
    fun findShouldReturnsEmptyIfNotFound() {
        val observer = database?.imageRoom()?.findForGame(2)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertValue { it.isEmpty() }
            assertNotComplete()
        }
    }

    @Test
    fun findShouldReturnData() {

        // Arrange
        val game = DomainFactory.makeGameCache()
        val data = makeRoomGameImage(gameId = game.id)
        val data2 = makeRoomGameImage(gameId = game.id)
        val data3 = makeRoomGameImage(gameId = game.id)

        database?.gamesDao()?.insert(game)

        database?.imageRoom()?.insert(data)
        database?.imageRoom()?.insert(data2)
        database?.imageRoom()?.insert(data3)

        val observer = database?.imageRoom()?.findForGame(game.id)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertValue { it.contains(data) && it.contains(data2) && it.contains(data3) && it.size == 3 }
            assertNotComplete()
        }
    }

    @After
    fun tearDown() {
        database?.close()
    }
}