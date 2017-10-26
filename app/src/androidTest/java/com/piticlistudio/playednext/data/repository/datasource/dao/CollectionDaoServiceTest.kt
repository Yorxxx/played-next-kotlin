package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.GameCollectionDao
import com.piticlistudio.playednext.factory.DomainFactory
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeCollectionDao
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectionDaoServiceTest {

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
    fun insert_shouldStoreCollectionDao() {

        val data = DomainFactory.makeCollectionDao()

        val result = database?.collectionDao()?.insert(data)

        Assert.assertNotNull(result)
        Assert.assertEquals(data.id, result!!.toInt())
    }

    @Test
    fun insertGameCollection_shouldStoreRelation() {
        val data = makeCollectionDao()
        val game = DomainFactory.makeGameCache()
        val relation = GameCollectionDao(game.id, data.id)

        database?.gamesDao()?.insertGame(game)
        database?.collectionDao()?.insert(data)
        val result = database?.collectionDao()?.insertGameCollection(relation)

        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun insertGameCollection_shouldUpdateRelation() {
        val data = makeCollectionDao()
        val data2 = makeCollectionDao()
        val game = DomainFactory.makeGameCache()
        val relation = GameCollectionDao(game.id, data.id)
        val updatedrelation = GameCollectionDao(game.id, data2.id)

        database?.gamesDao()?.insertGame(game)
        database?.collectionDao()?.insert(data)
        database?.collectionDao()?.insert(data2)
        database?.collectionDao()?.insertGameCollection(relation)
        val result = database?.collectionDao()?.insertGameCollection(updatedrelation)


        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun find_shouldThrowsErrorIfNotFound() {
        val observer = database?.collectionDao()?.find(2)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoValues()
            assertNotComplete()
            assertError { it is EmptyResultSetException }
        }
    }

    @Test
    fun find_shouldReturnStoredData() {
        val data = makeCollectionDao()

        database?.collectionDao()?.insert(data)

        val observer = database?.collectionDao()?.find(data.id.toLong())?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue(data)
        }
    }

    @Test
    fun findForGame_shouldReturnRelationData() {
        val game = DomainFactory.makeGameCache()
        val data1 = makeCollectionDao()
        val relation = GameCollectionDao(game.id, data1.id)

        database?.gamesDao()?.insertGame(game)
        database?.collectionDao()?.insert(data1)
        database?.collectionDao()?.insertGameCollection(relation)

        // Act
        val observer = database?.collectionDao()?.findForGame(game.id)?.test()

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
    fun findForGame_shouldThrowError() {

        val game = DomainFactory.makeGameCache()
        database?.gamesDao()?.insertGame(game)

        // Act
        // Act
        val observer = database?.collectionDao()?.findForGame(game.id)?.test()

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