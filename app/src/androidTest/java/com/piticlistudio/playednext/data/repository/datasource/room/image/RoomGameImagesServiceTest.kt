package com.piticlistudio.playednext.data.repository.datasource.room.image

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.database.sqlite.SQLiteConstraintException
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.repository.datasource.room.BaseRoomServiceTest
import com.piticlistudio.playednext.factory.GameImageFactory.Factory.makeRoomGameImage
import junit.framework.Assert
import junit.framework.Assert.*
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomGameImagesServiceTest: BaseRoomServiceTest() {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertShouldStoreData() {

        val data = makeRoomGameImage(gameId = getRandomStoredGameId())

        val result = database.imageRoom().insert(data)

        Assert.assertNotNull(result)
        assertEquals(data.id, result.toInt())
    }

    @Test
    fun insertIsNotAllowedIfGameDoesNotExistOnDatabase() {

        val data = makeRoomGameImage(gameId = -1)

        try {
            database.imageRoom().insert(data)
        } catch (e: Throwable) {
            assertNotNull(e)
            assertTrue(e is SQLiteConstraintException)
        }
    }

    @Test
    fun findShouldReturnData() {

        val observer = database.imageRoom().findForGame(getRandomStoredGameId()).test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertValue { it.isNotEmpty() }
            assertNotComplete()
        }
    }

    @After
    fun tearDown() {
        database.close()
    }
}