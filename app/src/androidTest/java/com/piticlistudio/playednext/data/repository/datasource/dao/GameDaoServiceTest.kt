package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Room
import android.database.sqlite.SQLiteConstraintException
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeGameCache
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameDaoServiceTest {

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
        observer?.apply {
            assertNoValues()
            assertNotComplete()
            assertError { it is EmptyResultSetException }
        }
    }

    @Test
    fun insertGameShouldStoreData() {

        val game = makeGameCache()

        val result = database?.gamesDao()?.insertGame(game)

        assertNotNull(result)
        assertEquals(game.id, result!!.toInt())
    }

    @Test
    fun insertGame_abortsIfAlreadyStored() {

        val game = makeGameCache()

        database?.gamesDao()?.insertGame(game)
        try {
            database?.gamesDao()?.insertGame(game)
            fail("should have thrown")
        } catch (e: Throwable) {
            assertNotNull(e)
            assertTrue(e is SQLiteConstraintException)
        }
    }

    @Test
    fun updateGame_shouldUpdateData() {

        val game = makeGameCache()
        val game2 = makeGameCache(game.id)

        database?.gamesDao()?.insertGame(game)
        val result = database?.gamesDao()?.updateGame(game2)

        assertNotNull(result)
        assertEquals(1, result)
    }

    @Test
    fun findGameByIdReturnsDataIfPresent() {
        val game = makeGameCache()

        database?.gamesDao()?.insertGame(game)

        val observer = database?.gamesDao()?.findGameById(game.id.toLong())?.test()

        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue(game)
        }
    }

    @Test
    fun getAllGamesReturnsAllStoredGames() {
        val game1 = makeGameCache()
        val game2 = makeGameCache()
        val game3 = makeGameCache()

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

    @After
    fun tearDown() {
        database?.close()
    }
}