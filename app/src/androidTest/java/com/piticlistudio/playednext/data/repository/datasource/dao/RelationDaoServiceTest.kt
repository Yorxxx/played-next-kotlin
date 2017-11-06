package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.GameRelationDao
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeGameCache
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makePlatformDao
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeRelationDao
import junit.framework.Assert
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.fail
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
        database?.gamesDao()?.insertGame(game)
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
        database?.gamesDao()?.insertGame(game)

        try {
            val data = makeRelationDao(game.id, platform.id)
            database?.relationDao()?.insert(data)
            fail("should have thrown")
        } catch (e: Throwable) {
        }
    }

    @Test
    fun findForGameAndPlatform_shouldReturnRelation() {
        val game = makeGameCache()
        val platform = makePlatformDao()
        database?.gamesDao()?.insertGame(game)
        database?.platformDao()?.insert(platform)
        val relation = makeRelationDao(game.id, platform.id)
        database?.relationDao()?.insert(relation)

        // Act
        val observer = database?.relationDao()?.findForGameAndPlatform(game.id, platform.id)?.test()
        Assert.assertNotNull(observer)
        observer?.apply {
            assertValueCount(1)
            assertValue(relation)
            assertComplete()
            assertNoErrors()
        }
    }

    @Test
    fun findForGameAndPlatform_shouldThrowIfNotFound() {
        val game = makeGameCache()
        val platform = makePlatformDao()
        database?.gamesDao()?.insertGame(game)
        database?.platformDao()?.insert(platform)
        val relation = makeRelationDao(game.id, platform.id)
        database?.relationDao()?.insert(relation)

        // Act
        val observer = database?.relationDao()?.findForGameAndPlatform(game.id, platform.id + 1)?.test()
        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoValues()
            assertNotComplete()
            assertError { it is EmptyResultSetException }
        }
    }

    @Test
    fun findForGame_shouldReturnAllRelationsForTheSpecifiedGame() {

        val game = makeGameCache()
        val game2 = makeGameCache()
        val platform = makePlatformDao()
        val platform2 = makePlatformDao()
        database?.gamesDao()?.insertGame(game)
        database?.gamesDao()?.insertGame(game2)
        database?.platformDao()?.insert(platform)
        database?.platformDao()?.insert(platform2)

        val relation = makeRelationDao(game.id, platform.id)
        val relation2 = makeRelationDao(game.id, platform2.id)
        database?.relationDao()?.insert(relation)
        database?.relationDao()?.insert(relation2)

        // Act
        val observer = database?.relationDao()?.findForGame(game.id)?.test()

        // Assert
        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertComplete()
            assertValue { it.size == 2 && it.containsAll(listOf<GameRelationDao>(relation, relation2)) }
        }
    }

    @Test
    fun findForGame_shouldEmptyListIfNoRelationsForThisGame() {

        val game = makeGameCache(1)
        val game2 = makeGameCache(2)
        val platform = makePlatformDao()
        val platform2 = makePlatformDao()
        database?.gamesDao()?.insertGame(game)
        database?.gamesDao()?.insertGame(game2)
        database?.platformDao()?.insert(platform)
        database?.platformDao()?.insert(platform2)

        val relation = makeRelationDao(game.id, platform.id)
        val relation2 = makeRelationDao(game.id, platform2.id)
        database?.relationDao()?.insert(relation)
        database?.relationDao()?.insert(relation2)

        // Act
        val observer = database?.relationDao()?.findForGame(15)?.test()

        // Assert
        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertComplete()
            assertValue { it.size == 0 }
        }
    }

    @After
    fun tearDown() {
        database?.close()
    }
}