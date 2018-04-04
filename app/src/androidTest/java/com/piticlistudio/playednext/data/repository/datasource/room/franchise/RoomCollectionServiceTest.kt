package com.piticlistudio.playednext.data.repository.datasource.room.franchise

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.room.RoomGameCollection
import com.piticlistudio.playednext.factory.DomainFactory
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeCollectionDao
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomCollectionServiceTest {

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

        val data = DomainFactory.makeCollectionDao()

        val result = database?.collectionRoom()?.insert(data)

        Assert.assertNotNull(result)
        Assert.assertEquals(data.id, result!!.toInt())
    }

    fun insertShouldIgnoreIfAlreadyStored() {

        val data = DomainFactory.makeCollectionDao()

        val id = database?.collectionRoom()?.insert(data)
        val id2 = database?.collectionRoom()?.insert(data)

        Assert.assertTrue(id!! > 0L)
        Assert.assertEquals(0L, id2)
    }

    @Test
    fun insertGameCollectionShouldStoreRelation() {
        val data = makeCollectionDao()
        val game = DomainFactory.makeGameCache()
        val relation = RoomGameCollection(game.id, data.id)

        database?.gamesDao()?.insert(game)
        database?.collectionRoom()?.insert(data)
        val result = database?.collectionRoom()?.insertGameCollection(relation)

        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun insertGameCollectionShouldUpdateExistingRelation() {
        val data = makeCollectionDao()
        val data2 = makeCollectionDao()
        val game = DomainFactory.makeGameCache()
        val relation = RoomGameCollection(game.id, data.id)
        val updatedrelation = RoomGameCollection(game.id, data2.id)

        database?.gamesDao()?.insert(game)
        database?.collectionRoom()?.insert(data)
        database?.collectionRoom()?.insert(data2)
        database?.collectionRoom()?.insertGameCollection(relation)
        val result = database?.collectionRoom()?.insertGameCollection(updatedrelation)


        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun findForGameShouldReturnRelationData() {
        val game = DomainFactory.makeGameCache()
        val data1 = makeCollectionDao()
        val relation = RoomGameCollection(game.id, data1.id)

        database?.gamesDao()?.insert(game)
        database?.collectionRoom()?.insert(data1)
        database?.collectionRoom()?.insertGameCollection(relation)

        // Act
        val observer = database?.collectionRoom()?.findForGame(game.id)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue {
                it == data1
            }
        }
    }

    @Test
    fun findForGameShouldThrowErrorIfNotFound() {

        val game = DomainFactory.makeGameCache()
        database?.gamesDao()?.insert(game)

        // Act
        // Act
        val observer = database?.collectionRoom()?.findForGame(game.id)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertError(EmptyResultSetException::class.java)
            assertNotComplete()
            assertNoValues()
        }
    }

    @After
    fun tearDown() {
        database?.close()
    }
}