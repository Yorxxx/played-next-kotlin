package com.piticlistudio.playednext.data.repository.datasource.room.platform

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.entity.room.RoomGamePlatform
import com.piticlistudio.playednext.data.repository.datasource.room.BaseRoomServiceTest
import com.piticlistudio.playednext.factory.PlatformFactory.Factory.makeRoomPlatform
import junit.framework.Assert
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomPlatformServiceTest : BaseRoomServiceTest() {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertShouldStoreData() {

        val data = makeRoomPlatform()

        val result = database.platformRoom().insert(data)

        Assert.assertNotNull(result)
        Assert.assertEquals(data.id, result.toInt())
    }

    @Test
    fun insertGamePlatformShouldStoreData() {
        val data = RoomGamePlatform(getRandomStoredGameId(), getRandomStoredPlatformId())

        val result = database.platformRoom().insertGamePlatform(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result > 0)
    }

    @Test
    fun findForGameShouldReturnData() {

        // Act
        val observer = database.platformRoom().findForGame(getRandomStoredGameId()).test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertNotComplete()
            assertValue { it.isNotEmpty() }
        }
    }

    @After
    fun tearDown() {
        database.close()
    }
}