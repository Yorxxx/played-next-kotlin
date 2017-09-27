package com.piticlistudio.playednext.data.cover.repository.local

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.cover.model.local.GameCover
import com.piticlistudio.playednext.data.game.model.local.LocalGame
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GameDaoTest {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule();

    private var database: AppDatabase? = null
    val storedGame = LocalGame(1, "name", "summary", "story")

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        database?.gamesDao()?.insertGame(storedGame)
    }

    @Test
    fun findCoverByGameIdThrowsErrorIfNotFound() {
        val observer = database?.coversDao()?.findCoverByGameId(0)?.test()

        Assert.assertNotNull(observer)
        with(observer) {
            this!!.assertNoValues()
            assertNotComplete()
            assertError { it is EmptyResultSetException }
        }
    }

    @Test
    fun insertCoverShouldStoreData() {

        val cover = GameCover("url", storedGame.id, 50, 50)

        val result = database?.coversDao()?.insertCover(cover)

        assertNotNull(result)
        assertEquals(1L, result)
    }

    @Test
    fun insertCoverReplacesOnConflict() {
        val cover = GameCover("url", storedGame.id, 50, 50)
        val cover2 = GameCover("url", storedGame.id, 500, 50)

        database?.coversDao()?.insertCover(cover)
        database?.coversDao()?.insertCover(cover2)

        val observer = database?.coversDao()?.findCoverByGameId(storedGame.id)?.test()

        assertNotNull(observer)
        with(observer) {
            this!!.assertValue { it.width == cover2.width }
        }
    }

    @Test
    fun findCoverByGameIdReturnsDataIfPresent() {
        val cover = GameCover("url", storedGame.id, 50, 50)

        database?.coversDao()?.insertCover(cover)

        val observer = database?.coversDao()?.findCoverByGameId(storedGame.id)?.test()

        Assert.assertNotNull(observer)
        with(observer) {
            this!!.assertComplete()
            assertNoErrors()
            assertValueCount(1)
            assertValue(cover)
        }
    }

    @After
    fun tearDown() {
        database?.close()
    }
}