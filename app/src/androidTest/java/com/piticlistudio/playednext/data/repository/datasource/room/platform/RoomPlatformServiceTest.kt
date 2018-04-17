package com.piticlistudio.playednext.data.repository.datasource.room.platform

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.room.RoomGamePlatform
import com.piticlistudio.playednext.factory.DomainFactory
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeRoomPlatform
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomPlatformServiceTest {

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

        val data = makeRoomPlatform()

        val result = database?.platformRoom()?.insert(data)

        Assert.assertNotNull(result)
        Assert.assertEquals(data.id, result!!.toInt())
    }

    fun insertShouldIgnoreIfAlreadyStored() {

        val data = makeRoomPlatform()

        val id = database?.platformRoom()?.insert(data)
        val id2 = database?.platformRoom()?.insert(data)

        Assert.assertTrue(id!! > 0L)
        Assert.assertEquals(0L, id2)
    }

    @Test
    fun insertGamePlatformShouldStoreData() {
        val platform = makeRoomPlatform()
        val game = DomainFactory.makeGameCache()
        val data = RoomGamePlatform(game.id, platform.id)

        database?.gamesDao()?.insert(game)
        database?.platformRoom()?.insert(platform)
        val result = database?.platformRoom()?.insertGamePlatform(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun insertGamePlatformShouldReplaceDataOnConflict() {
        val platform = makeRoomPlatform()
        val game = DomainFactory.makeGameCache()
        val data = RoomGamePlatform(game.id, platform.id)

        database?.gamesDao()?.insert(game)
        database?.platformRoom()?.insert(platform)
        database?.platformRoom()?.insertGamePlatform(data)
        val result = database?.platformRoom()?.insertGamePlatform(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun findForGameShouldReturnData() {
        val game = DomainFactory.makeGameCache()
        val game2 = DomainFactory.makeGameCache()
        val platform1 = makeRoomPlatform()
        val platform2 = makeRoomPlatform()
        val data = RoomGamePlatform(game.id, platform1.id)
        val data2 = RoomGamePlatform(game.id, platform2.id)
        val data3 = RoomGamePlatform(game2.id, platform1.id)

        database?.gamesDao()?.insert(game)
        database?.gamesDao()?.insert(game2)
        database?.platformRoom()?.insert(platform1)
        database?.platformRoom()?.insert(platform2)
        database?.platformRoom()?.insertGamePlatform(data)
        database?.platformRoom()?.insertGamePlatform(data2)
        database?.platformRoom()?.insertGamePlatform(data3)

        // Act
        val observer = database?.platformRoom()?.findForGame(game.id)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertNotComplete()
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
        val observer = database?.platformRoom()?.findForGame(game.id)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertNotComplete()
            assertValue { it.size == 0 }
        }
    }

    @After
    fun tearDown() {
        database?.close()
    }
}