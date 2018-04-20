package com.piticlistudio.playednext.data.repository.datasource.room

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.database.sqlite.SQLiteConstraintException
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeRoomGame
import junit.framework.Assert.*
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomGameServiceTest : BaseRoomServiceTest() {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Test
    fun findByIdShouldReturnEmptyListWhenNoMatches() {
        val observer = database.gamesDao().findById(-1).test()

        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertNotComplete()
            assertValue { it.isEmpty() }
        }
    }

    @Test
    fun findByIdShouldReturnData() {

        val observer = database.gamesDao().findById(getRandomStoredGameId().toLong()).test()

        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertNotComplete()
            assertValue { it.size == 1 }
        }
    }

    @Test
    fun insertShouldStoreData() {

        val game = makeRoomGame()

        val result = database.gamesDao().insert(game)

        assertNotNull(result)
        assertEquals(game.id, result.toInt())
    }

    @Test
    fun insertShouldabortIfAlreadyStored() {

        val id = getRandomStoredGameId()
        val game = makeRoomGame(id)

        try {
            database.gamesDao().insert(game)
            fail("should have thrown")
        } catch (e: Throwable) {
            assertNotNull(e)
            assertTrue(e is SQLiteConstraintException)
        }
    }

    @Test
    fun updateShouldUpdateData() {

        val id = getRandomStoredGameId()
        val game = makeRoomGame(id)

        val result = database.gamesDao().update(game)

        assertNotNull(result)
        assertEquals(1, result)
    }

    @Test
    fun updateShouldAbortIfDataIsNotStored() {

        val id = getStoredGameIds().min()?.minus(1)
        val game = makeRoomGame(id = id!!)

        val result = database.gamesDao().update(game)
        assertEquals(0, result)
    }

    @Test
    fun findAllShouldReturnAllStoredGames() {
        val observer = database.gamesDao().findAll().test()

        assertNotNull(observer)
        observer?.apply {
            assertNotComplete()
            assertValueCount(1)
            assertNoErrors()
            assertValue { it.size == getStoredGameIds().size }
        }
    }

    @After
    fun tearDown() {
        database.close()
    }
}