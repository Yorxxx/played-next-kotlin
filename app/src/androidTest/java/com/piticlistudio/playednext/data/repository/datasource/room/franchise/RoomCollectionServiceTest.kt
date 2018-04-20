package com.piticlistudio.playednext.data.repository.datasource.room.franchise

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.room.RoomGameCollection
import com.piticlistudio.playednext.data.repository.datasource.room.BaseRoomServiceTest
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeRoomCollection
import com.piticlistudio.playednext.factory.DataFactory
import com.piticlistudio.playednext.test.factory.GameFactory
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomCollectionServiceTest: BaseRoomServiceTest() {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertShouldStoreData() {

        val data = makeRoomCollection()

        val result = database.collectionRoom().insert(data)

        Assert.assertNotNull(result)
        Assert.assertEquals(data.id, result.toInt())
    }

    @Test
    fun insertGameCollectionShouldStoreRelation() {
        val relation = RoomGameCollection(getRandomStoredGameId(), getRandomStoredCollectionId())

        val result = database.collectionRoom().insertGameCollection(relation)

        Assert.assertNotNull(result)
        Assert.assertTrue(result > 0)
    }

    @Test
    fun findForGameShouldReturnRelationData() {
        // Act
        val observer = database.collectionRoom().findForGame(getRandomStoredGameId()).test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertNotComplete()
            assertValue { !it.isEmpty() }
        }
    }

    @After
    fun tearDown() {
        database.close()
    }
}