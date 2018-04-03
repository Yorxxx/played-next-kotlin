package com.piticlistudio.playednext.data.repository.datasource.room.company

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.room.RoomGameDeveloper
import com.piticlistudio.playednext.data.entity.room.RoomGamePublisher
import com.piticlistudio.playednext.factory.DomainFactory
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class RoomCompanyServiceTest {

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

        val data = DomainFactory.makeCompanyDao()

        val result = database?.companyRoom()?.insert(data)

        Assert.assertNotNull(result)
        Assert.assertEquals(data.id, result!!.toInt())
    }

    fun insertShouldIgnoreIfAlreadyStored() {

        val data = DomainFactory.makeCompanyDao()

        val id = database?.companyRoom()?.insert(data)
        val id2 = database?.companyRoom()?.insert(data)

        Assert.assertTrue(id!! > 0L)
        Assert.assertEquals(0L, id2)
    }

    @Test
    fun insertGameDeveloperShouldStoreData() {
        val company = DomainFactory.makeCompanyDao()
        val game = DomainFactory.makeGameCache()
        val data = RoomGameDeveloper(game.id, company.id)

        database?.gamesDao()?.insert(game)
        database?.companyRoom()?.insert(company)
        val result = database?.companyRoom()?.insertGameDeveloper(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun insertGamePublisherShouldStoreData() {
        val company = DomainFactory.makeCompanyDao()
        val game = DomainFactory.makeGameCache()
        val data = RoomGamePublisher(game.id, company.id)

        database?.gamesDao()?.insert(game)
        database?.companyRoom()?.insert(company)
        val result = database?.companyRoom()?.insertGamePublisher(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun insertGameDeveloperShouldReplaceDataOnConflict() {
        val company = DomainFactory.makeCompanyDao()
        val game = DomainFactory.makeGameCache()
        val data = RoomGameDeveloper(game.id, company.id)

        database?.gamesDao()?.insert(game)
        database?.companyRoom()?.insert(company)
        database?.companyRoom()?.insertGameDeveloper(data)
        val result = database?.companyRoom()?.insertGameDeveloper(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun insertGamePublisherShouldReplaceDataOnConflict() {
        val company = DomainFactory.makeCompanyDao()
        val game = DomainFactory.makeGameCache()
        val data = RoomGamePublisher(game.id, company.id)

        database?.gamesDao()?.insert(game)
        database?.companyRoom()?.insert(company)
        database?.companyRoom()?.insertGamePublisher(data)
        val result = database?.companyRoom()?.insertGamePublisher(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result!! > 0)
    }

    @Test
    fun findShouldThrowErrorIfNotFound() {
        val observer = database?.companyRoom()?.find(2)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoValues()
            assertNotComplete()
            assertError { it is EmptyResultSetException }
        }
    }

    @Test
    fun findShouldReturnData() {
        val data = DomainFactory.makeCompanyDao()

        database?.companyRoom()?.insert(data)

        val observer = database?.companyRoom()?.find(data.id.toLong())?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue(data)
        }
    }

    @Test
    fun findDeveloperForGameShouldReturnData() {
        val game = DomainFactory.makeGameCache()
        val game2 = DomainFactory.makeGameCache()
        val company1 = DomainFactory.makeCompanyDao()
        val company2 = DomainFactory.makeCompanyDao()
        val data = RoomGameDeveloper(game.id, company1.id)
        val data2 = RoomGameDeveloper(game.id, company2.id)
        val data3 = RoomGameDeveloper(game2.id, company1.id)

        database?.gamesDao()?.insert(game)
        database?.gamesDao()?.insert(game2)
        database?.companyRoom()?.insert(company1)
        database?.companyRoom()?.insert(company2)
        database?.companyRoom()?.insertGameDeveloper(data)
        database?.companyRoom()?.insertGameDeveloper(data2)
        database?.companyRoom()?.insertGameDeveloper(data3)

        // Act
        val observer = database?.companyRoom()?.findDeveloperForGame(game.id)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue {
                it.size == 2 && it.contains(company1) && it.contains(company2)
            }
        }
    }

    @Test
    fun findPublishersForGameShouldReturnData() {
        val game = DomainFactory.makeGameCache()
        val game2 = DomainFactory.makeGameCache()
        val company1 = DomainFactory.makeCompanyDao()
        val company2 = DomainFactory.makeCompanyDao()
        val data = RoomGamePublisher(game.id, company1.id)
        val data2 = RoomGamePublisher(game.id, company2.id)
        val data3 = RoomGamePublisher(game2.id, company1.id)

        database?.gamesDao()?.insert(game)
        database?.gamesDao()?.insert(game2)
        database?.companyRoom()?.insert(company1)
        database?.companyRoom()?.insert(company2)
        database?.companyRoom()?.insertGamePublisher(data)
        database?.companyRoom()?.insertGamePublisher(data2)
        database?.companyRoom()?.insertGamePublisher(data3)

        // Act
        val observer = database?.companyRoom()?.findPublishersForGame(game.id)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue {
                it.size == 2 && it.contains(company1) && it.contains(company2)
            }
        }
    }

    @Test
    fun findDeveloperForGameShouldReturnEmptyListWhenNoMatches() {

        val game = DomainFactory.makeGameCache()
        database?.gamesDao()?.insert(game)

        // Act
        // Act
        val observer = database?.companyRoom()?.findDeveloperForGame(game.id)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue { it.isEmpty() }
        }
    }

    @Test
    fun findPublishersForGameShouldReturnEmptyListWhenNoMatches() {

        val game = DomainFactory.makeGameCache()
        database?.gamesDao()?.insert(game)

        // Act
        // Act
        val observer = database?.companyRoom()?.findPublishersForGame(game.id)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue { it.isEmpty() }
        }
    }

    @After
    fun tearDown() {
        database?.close()
    }
}