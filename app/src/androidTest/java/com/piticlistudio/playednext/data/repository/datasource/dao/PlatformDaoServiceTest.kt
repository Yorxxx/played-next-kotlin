package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.GamePlatformDao
import com.piticlistudio.playednext.factory.DomainFactory
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makePlatformDao
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlatformDaoServiceTest {

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

        val data = makePlatformDao()

        val result = database?.platformDao()?.insert(data)

        Assert.assertNotNull(result)
        Assert.assertEquals(data.id, result!!.toInt())
    }

    fun insertShouldIgnoreIfAlreadyStored() {

        val data = makePlatformDao()

        val id = database?.platformDao()?.insert(data)
        val id2 = database?.platformDao()?.insert(data)

        Assert.assertTrue(id!! > 0L)
        Assert.assertEquals(0L, id2)
    }

    @Test
    fun insertGamePlatformShouldStoreData() {
        val platform = makePlatformDao()
        val game = DomainFactory.makeGameCache()
        val data = GamePlatformDao(game.id, platform.id)

        database?.gamesDao()?.insert(game)
        database?.platformDao()?.insert(platform)
        val result = database?.platformDao()?.insertGamePlatform(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun insertGamePlatformShouldReplaceDataOnConflict() {
        val platform = makePlatformDao()
        val game = DomainFactory.makeGameCache()
        val data = GamePlatformDao(game.id, platform.id)

        database?.gamesDao()?.insert(game)
        database?.platformDao()?.insert(platform)
        database?.platformDao()?.insertGamePlatform(data)
        val result = database?.platformDao()?.insertGamePlatform(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun findShouldThrowsErrorIfNotFound() {
        val observer = database?.platformDao()?.find(2)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoValues()
            assertNotComplete()
            assertError { it is EmptyResultSetException }
        }
    }

    @Test
    fun findShouldReturnData() {
        val data = makePlatformDao()

        database?.platformDao()?.insert(data)

        val observer = database?.platformDao()?.find(data.id.toLong())?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue(data)
        }
    }

    @Test
    fun findForGameShouldReturnData() {
        val game = DomainFactory.makeGameCache()
        val game2 = DomainFactory.makeGameCache()
        val platform1 = makePlatformDao()
        val platform2 = makePlatformDao()
        val data = GamePlatformDao(game.id, platform1.id)
        val data2 = GamePlatformDao(game.id, platform2.id)
        val data3 = GamePlatformDao(game2.id, platform1.id)

        database?.gamesDao()?.insert(game)
        database?.gamesDao()?.insert(game2)
        database?.platformDao()?.insert(platform1)
        database?.platformDao()?.insert(platform2)
        database?.platformDao()?.insertGamePlatform(data)
        database?.platformDao()?.insertGamePlatform(data2)
        database?.platformDao()?.insertGamePlatform(data3)

        // Act
        val observer = database?.platformDao()?.findForGame(game.id)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue {
                it.size == 2 && it.contains(platform1) && it.contains(platform2)
            }
        }
    }

    @Test
    fun findForGameShouldReturnEmptyListWhenNoMatches() {

        val game = DomainFactory.makeGameCache()
        database?.gamesDao()?.insert(game)

        // Act
        // Act
        val observer = database?.platformDao()?.findForGame(game.id)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue { it.size == 0 }
        }
    }

    @After
    fun tearDown() {
        database?.close()
    }
}