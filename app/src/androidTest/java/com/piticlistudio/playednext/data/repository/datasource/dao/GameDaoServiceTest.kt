package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.database.sqlite.SQLiteConstraintException
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.GameDeveloperDao
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeCompanyDao
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
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var database: AppDatabase? = null

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @Test
    fun findByIdShouldReturnEmptyListWhenNoMatches() {
        val observer = database?.gamesDao()?.findById(2)?.test()

        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertNotComplete()
            assertValue { it.isEmpty() }
        }
    }

    @Test
    fun findByIdShouldReturnDataIfPresent() {
        val game = makeGameCache()

        database?.gamesDao()?.insert(game)

        val observer = database?.gamesDao()?.findById(game.id.toLong())?.test()

        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertNotComplete()
            assertValue { it.size == 1 && it.contains(game) }
        }
    }

    @Test
    fun insertShouldStoreData() {

        val game = makeGameCache()

        val result = database?.gamesDao()?.insert(game)

        assertNotNull(result)
        assertEquals(game.id, result!!.toInt())
    }

    @Test
    fun insertShouldabortIfAlreadyStored() {

        val game = makeGameCache()

        database?.gamesDao()?.insert(game)
        try {
            database?.gamesDao()?.insert(game)
            fail("should have thrown")
        } catch (e: Throwable) {
            assertNotNull(e)
            assertTrue(e is SQLiteConstraintException)
        }
    }

    @Test
    fun updateShouldUpdateData() {

        val game = makeGameCache()
        val game2 = makeGameCache(game.id)

        database?.gamesDao()?.insert(game)
        val result = database?.gamesDao()?.update(game2)

        assertNotNull(result)
        assertEquals(1, result)
    }

    @Test
    fun updateShouldAbortIfDataIsNotStored() {

        val game = makeGameCache()

        val result = database?.gamesDao()?.update(game)
        assertEquals(0, result)
    }

    @Test
    fun findAllShouldReturnAllStoredGames() {
        val game1 = makeGameCache()
        val game2 = makeGameCache()
        val game3 = makeGameCache()

        database?.gamesDao()?.insert(game1)
        database?.gamesDao()?.insert(game2)
        database?.gamesDao()?.insert(game3)

        val observer = database?.gamesDao()?.findAll()?.test()

        assertNotNull(observer)
        observer?.apply {
            assertNotComplete()
            assertValueCount(1)
            assertNoErrors()
            assertValue { it.size == 3 && it.containsAll(listOf(game1, game2, game3)) }
        }
    }

    @Test
    fun loadById() {

        val developer1 = makeCompanyDao()
        val developer2 = makeCompanyDao()
        val developer3 = makeCompanyDao()
        database?.companyDao()?.insert(developer1)
        database?.companyDao()?.insert(developer2)
        database?.companyDao()?.insert(developer3)

        val game = makeGameCache()
        database?.gamesDao()?.insert(game)

        val relation = GameDeveloperDao(game.id, developer1.id)
        val relation2 = GameDeveloperDao(game.id, developer2.id)
        val relation3 = GameDeveloperDao(game.id, developer3.id)
        database?.companyDao()?.insertGameDeveloper(relation)
        database?.companyDao()?.insertGameDeveloper(relation2)
        database?.companyDao()?.insertGameDeveloper(relation3)

        val observer = database?.gamesDao()?.loadById(game.id.toLong())?.test()

        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertNotComplete()
            val relations = values().first()
            assertEquals(3, relations.size)
            relations.forEach {
                assertEquals(game, it.game)
            }
            assertEquals(relations.first().developer(), developer1)
            assertEquals(relations.get(1).developer(), developer2)
            assertEquals(relations.get(2).developer(), developer3)
            //assertEquals(listOf(relations.first().developer(), relations.get(1).developer(), relations.get(2).developer()),
              //      listOf(developer1, developer2, developer3))
        }
    }

    @After
    fun tearDown() {
        database?.close()
    }
}