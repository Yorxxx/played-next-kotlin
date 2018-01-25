package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.database.sqlite.SQLiteConstraintException
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.*
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeCollectionDao
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeCompanyDao
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeGameCache
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeGenreDao
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makePlatformDao
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
    fun loadById_shouldReturnGameAndRelationships() {

        val developer1 = makeCompanyDao()
        val developer2 = makeCompanyDao()
        val developer3 = makeCompanyDao()
        val publisher1 = makeCompanyDao()
        val publisher2 = makeCompanyDao()
        database?.companyDao()?.insert(developer1)
        database?.companyDao()?.insert(developer2)
        database?.companyDao()?.insert(developer3)
        database?.companyDao()?.insert(publisher1)
        database?.companyDao()?.insert(publisher2)

        val genre1 = makeGenreDao()
        database?.genreDao()?.insert(genre1)

        val collection = makeCollectionDao()
        database?.collectionDao()?.insert(collection)

        val platform1 = makePlatformDao()
        val platform2 = makePlatformDao()
        database?.platformDao()?.insert(platform1)
        database?.platformDao()?.insert(platform2)

        val game = makeGameCache()
        database?.gamesDao()?.insert(game)

        val relation = GameDeveloperDao(game.id, developer1.id)
        val relation2 = GameDeveloperDao(game.id, developer2.id)
        val relation3 = GameDeveloperDao(game.id, developer3.id)
        database?.companyDao()?.insertGameDeveloper(relation)
        database?.companyDao()?.insertGameDeveloper(relation2)
        database?.companyDao()?.insertGameDeveloper(relation3)
        database?.companyDao()?.insertGamePublisher(GamePublisherDao(game.id, publisher1.id))
        database?.companyDao()?.insertGamePublisher(GamePublisherDao(game.id, publisher2.id))
        database?.genreDao()?.insertGameGenre(GameGenreDao(game.id, genre1.id))
        database?.collectionDao()?.insertGameCollection(GameCollectionDao(game.id, collection.id))
        database?.platformDao()?.insertGamePlatform(GamePlatformDao(game.id, platform1.id))
        database?.platformDao()?.insertGamePlatform(GamePlatformDao(game.id, platform2.id))

        val observer = database?.gamesDao()?.loadById(game.id.toLong())?.test()

        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertNotComplete()
            val relations = values().first()
            assertEquals("Should have only one element", 1, relations.size)
            //assertEquals("Should have game entity", game, relations.first().game)
            assertEquals("Should have three developers", 3, relations.first().companyIdList?.size)
            assertEquals("Should have two publishers", 2, relations.first().publisherIdList?.size)
            assertEquals("Should have one genre", 1, relations.first().genreIdList?.size)
            assertEquals("Should have one collection", 1, relations.first().collectionIdList?.size)
            assertEquals("Should have two platforms", 2, relations.first().platformIdIdList?.size)
            assertEquals("Should have no screenshots", 0, relations.first().screenshots?.size)
        }
    }

    @After
    fun tearDown() {
        database?.close()
    }
}