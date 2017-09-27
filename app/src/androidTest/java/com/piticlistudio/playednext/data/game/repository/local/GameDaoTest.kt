package com.piticlistudio.playednext.data.game.repository.local

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.game.model.local.LocalGame
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers

@RunWith(AndroidJUnit4::class)
class GameDaoTest {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule();

    private var database: AppDatabase? = null

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @Test
    fun findGameByIdThrowsErrorIfNotFound() {
        val observer = database?.gamesDao()?.findGameById(2)?.test()

        assertNotNull(observer)
        with(observer) {
            this!!.assertNoValues()
            assertNotComplete()
            assertError { it is EmptyResultSetException }
        }
    }

    @Test
    fun insertGameShouldStoreData() {

        val game = LocalGame(1, "name", "summary", "story")

        val result = database?.gamesDao()?.insertGame(game)

        assertNotNull(result)
        assertEquals(1L, result)
    }

    @Test
    fun insertGameReplacesOnConflict() {
        val game = LocalGame(1, "name", "summary", "story")
        val game2 = LocalGame(1, "name", "summary", "story")

        database?.gamesDao()?.insertGame(game)
        database?.gamesDao()?.insertGame(game2)

        val observer = database?.gamesDao()?.getAllGames()?.test()

        assertNotNull(observer)
        with(observer) {
            this!!.assertValue { it.size == 1 }
        }
    }

    @Test
    fun findGameByIdReturnsDataIfPresent() {
        val game = LocalGame(1, "name", "summary", "story")

        database?.gamesDao()?.insertGame(game)

        val observer = database?.gamesDao()?.findGameById(1)?.test()

        assertNotNull(observer)
        with(observer) {
            this!!.assertComplete()
            assertNoErrors()
            assertValueCount(1)
            assertValue(game)
        }
    }

    @Test
    fun getAllGamesReturnsAllStoredGames() {
        val game1 = LocalGame(1, "name", "summary", "story")
        val game2 = LocalGame(2, "name", "summary", "story")
        val game3 = LocalGame(3, "name", "summary", "story")

        database?.gamesDao()?.insertGame(game1)
        database?.gamesDao()?.insertGame(game2)
        database?.gamesDao()?.insertGame(game3)

        val observer = database?.gamesDao()?.getAllGames()?.test()

        assertNotNull(observer)
        with(observer) {
            this!!.assertNotComplete()
            assertNoErrors()
            assertValueCount(1)
            assertValue { it.size == 3 && it.containsAll(listOf(game1, game2, game3)) }
        }
    }

    @Test
    fun deleteGame() {
        val game1 = LocalGame(1, "name", "summary", "story")

        database?.gamesDao()?.insertGame(game1)

        var observer = database?.gamesDao()?.getAllGames()?.test()

        assertNotNull(observer)
        with(observer) {
            this!!.assertValue { it.size == 1 }
        }

        // Act
        database?.gamesDao()?.deleteGame(game1)

        observer = database?.gamesDao()?.getAllGames()?.test()

        assertNotNull(observer)
        with(observer) {
            this!!.assertValue { it.size == 0 }
        }

    }

    @After
    fun tearDown() {
        database?.close()
    }
}