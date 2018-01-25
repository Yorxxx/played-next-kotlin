package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeGameCache
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makePlatformDao
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeRelationDao
import com.piticlistudio.playednext.factory.DomainFactory.Factory.randomInt
import junit.framework.Assert
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RelationDaoServiceTest {

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
    fun insert_shouldStore() {

        val game = makeGameCache()
        val platform = makePlatformDao()
        database?.gamesDao()?.insert(game)
        database?.platformDao()?.insert(platform)

        val data = makeRelationDao(game.id, platform.id)

        val result = database?.relationDao()?.insert(data)

        Assert.assertNotNull(result)
    }

    @Test
    fun insert_failsIfGameIsNotStored() {

        val game = makeGameCache()
        val platform = makePlatformDao()
        database?.platformDao()?.insert(platform)

        try {
            val data = makeRelationDao(game.id, platform.id)
            database?.relationDao()?.insert(data)
            fail("should have thrown")
        } catch (e: Throwable) {
        }
    }

    @Test
    fun insert_failsIfPlatformIsNotStored() {

        val game = makeGameCache()
        val platform = makePlatformDao()
        database?.gamesDao()?.insert(game)

        try {
            val data = makeRelationDao(game.id, platform.id)
            database?.relationDao()?.insert(data)
            fail("should have thrown")
        } catch (e: Throwable) {
        }
    }

    @Test
    fun load_shouldReturnsStatusAndGame() {
        val game = makeGameCache()
        val platform = makePlatformDao()
        database?.gamesDao()?.insert(game)
        database?.platformDao()?.insert(platform)
        val relation = makeRelationDao(game.id, platform.id)
        database?.relationDao()?.insert(relation)

        // Act
        val observer = database?.relationDao()?.load(game.id)?.test()
        Assert.assertNotNull(observer)
        observer?.apply {
            assertValueCount(1)
            assertNotComplete()
            assertNoErrors()
            val data = values().first()
            assertEquals("Should have one game", 1, data.first().game?.size)
            assertEquals("Should contain game data", game, data.first().game?.first())
            assertEquals("Should contain status data", relation, data.first().data)
            assertEquals("Should have one platform", 1, data.first().platform?.size)
            assertEquals("Should contain platform data", platform, data.first().platform?.first())
        }
    }

    @Test
    fun load_returnsEmptyListIfNoMatch() {
        val game = makeGameCache()
        val platform = makePlatformDao()
        database?.gamesDao()?.insert(game)
        database?.platformDao()?.insert(platform)
        val relation = makeRelationDao(game.id, platform.id)
        database?.relationDao()?.insert(relation)

        // Act
        val observer = database?.relationDao()?.load(game.id + 1)?.test()
        Assert.assertNotNull(observer)
        observer?.apply {
            assertValueCount(1)
            assertNotComplete()
            assertNoErrors()
            assertValue { it.isEmpty() }
        }
    }

    @Test
    fun loadAll_returnsEmptyList() {

        // Act
        val observer = database?.relationDao()?.loadAll()?.test()
        Assert.assertNotNull(observer)
        observer?.apply {
            assertValueCount(1)
            assertNotComplete()
            assertNoErrors()
            assertValue { it.isEmpty() }
        }
    }

    @Test
    fun loadAll_returnsAllRelationsList() {

        repeat(10) {
            val game = makeGameCache()
            val platform = makePlatformDao()
            database?.gamesDao()?.insert(game)
            database?.platformDao()?.insert(platform)
            val relation = makeRelationDao(game.id, platform.id)
            database?.relationDao()?.insert(relation)
        }

        // Act
        val observer = database?.relationDao()?.loadAll()?.test()
        Assert.assertNotNull(observer)
        observer?.apply {
            assertValueCount(1)
            assertNotComplete()
            assertNoErrors()
            assertValue { it.size == 10 }
            this.values().first().forEach {
                assertNotNull(it.data)
                assertNotNull(it.game)
                assertEquals(1, it.game!!.size)
                assertNotNull(it.platform)
                assertEquals(1, it.platform!!.size)
            }
        }
    }

    @Test
    fun loadForGameAndPlatform_returnsEmptyList() {
        // Act
        val observer = database?.relationDao()?.loadForGameAndPlatform(randomInt(), randomInt())?.test()
        Assert.assertNotNull(observer)
        observer?.apply {
            assertValueCount(1)
            assertNotComplete()
            assertNoErrors()
            assertValue { it.isEmpty() }
        }
    }

    @Test
    fun loadForGameAndPlatform_returnsAllRelationsList() {

        repeat(10) {
            val game = makeGameCache()
            val platform = makePlatformDao()
            database?.gamesDao()?.insert(game)
            database?.platformDao()?.insert(platform)
            val relation = makeRelationDao(game.id, platform.id)
            database?.relationDao()?.insert(relation)
        }

        val gameId = 100
        val platformId = 200
        val game = makeGameCache(gameId)
        val platform = makePlatformDao(platformId)
        database?.gamesDao()?.insert(game)
        database?.platformDao()?.insert(platform)
        val relation = makeRelationDao(game.id, platform.id)
        database?.relationDao()?.insert(relation)

        // Act
        val observer = database?.relationDao()?.loadForGameAndPlatform(gameId, platformId)?.test()
        Assert.assertNotNull(observer)
        observer?.apply {
            assertValueCount(1)
            assertNotComplete()
            assertNoErrors()
            assertValue { it.size == 1 }
            this.values().first().forEach {
                assertNotNull(it)
                assertNotNull(it.game)
                assertEquals(1, it.game!!.size)
                assertEquals(game, it.game!!.first())
                assertNotNull(it.platform)
                assertEquals(1, it.platform!!.size)
                assertEquals(platform, it.platform!!.first())
                assertEquals(relation, it.data)
            }
        }
    }

    @After
    fun tearDown() {
        database?.close()
    }
}