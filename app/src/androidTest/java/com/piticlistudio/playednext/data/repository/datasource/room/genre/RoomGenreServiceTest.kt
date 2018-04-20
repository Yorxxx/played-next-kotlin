package com.piticlistudio.playednext.data.repository.datasource.room.genre

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.room.RoomGameGenre
import com.piticlistudio.playednext.data.repository.datasource.room.BaseRoomServiceTest
import com.piticlistudio.playednext.data.repository.datasource.room.RoomGameServiceTest
import com.piticlistudio.playednext.factory.DataFactory
import com.piticlistudio.playednext.test.factory.GameFactory
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeRoomGenre
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class RoomGenreServiceTest: BaseRoomServiceTest() {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertShouldStoreData() {

        val data = makeRoomGenre()

        val result = database.genreRoom().insert(data)

        Assert.assertNotNull(result)
        Assert.assertEquals(data.id, result.toInt())
    }


    @Test
    fun insertGameGenreShouldStoreData() {

        val data = RoomGameGenre(getRandomStoredGameId(), getRandomStoredGenreId())

        val result = database.genreRoom().insertGameGenre(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result > 0)
    }

    @Test
    fun findForGameShouldReturnData() {

        // Act
        val observer = database.genreRoom().findForGame(getRandomStoredGameId()).test()

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