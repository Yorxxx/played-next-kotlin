package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.GameDeveloperDao
import com.piticlistudio.playednext.data.entity.dao.GamePublisherDao
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeCompanyDao
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeGameCache
import junit.framework.Assert
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CompanyDaoServiceTest {

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

        val data = makeCompanyDao()

        val result = database?.companyDao()?.insert(data)

        assertNotNull(result)
        assertEquals(data.id, result!!.toInt())
    }

    fun insertShouldIgnoreIfAlreadyStored() {

        val data = makeCompanyDao()

        val id = database?.companyDao()?.insert(data)
        val id2 = database?.companyDao()?.insert(data)

        assertTrue(id!! > 0L)
        assertEquals(0L, id2)
    }

    @Test
    fun insertGameDeveloperShouldStoreData() {
        val company = makeCompanyDao()
        val game = makeGameCache()
        val data = GameDeveloperDao(game.id, company.id)

        database?.gamesDao()?.insert(game)
        database?.companyDao()?.insert(company)
        val result = database?.companyDao()?.insertGameDeveloper(data)

        assertNotNull(result)
        assertTrue(result!! > 0)
    }

    @Test
    fun insertGamePublisherShouldStoreData() {
        val company = makeCompanyDao()
        val game = makeGameCache()
        val data = GamePublisherDao(game.id, company.id)

        database?.gamesDao()?.insert(game)
        database?.companyDao()?.insert(company)
        val result = database?.companyDao()?.insertGamePublisher(data)

        assertNotNull(result)
        assertTrue(result!! > 0)
    }

    @Test
    fun insertGameDeveloperShouldReplaceDataOnConflict() {
        val company = makeCompanyDao()
        val game = makeGameCache()
        val data = GameDeveloperDao(game.id, company.id)

        database?.gamesDao()?.insert(game)
        database?.companyDao()?.insert(company)
        database?.companyDao()?.insertGameDeveloper(data)
        val result = database?.companyDao()?.insertGameDeveloper(data)

        assertNotNull(result)
        assertTrue(result!! > 0)
    }

    @Test
    fun insertGamePublisherShouldReplaceDataOnConflict() {
        val company = makeCompanyDao()
        val game = makeGameCache()
        val data = GamePublisherDao(game.id, company.id)

        database?.gamesDao()?.insert(game)
        database?.companyDao()?.insert(company)
        database?.companyDao()?.insertGamePublisher(data)
        val result = database?.companyDao()?.insertGamePublisher(data)

        assertNotNull(result)
        assertTrue(result!! > 0)
    }

    @Test
    fun findShouldThrowErrorIfNotFound() {
        val observer = database?.companyDao()?.find(2)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoValues()
            assertNotComplete()
            assertError { it is EmptyResultSetException }
        }
    }

    @Test
    fun findShouldReturnData() {
        val data = makeCompanyDao()

        database?.companyDao()?.insert(data)

        val observer = database?.companyDao()?.find(data.id.toLong())?.test()

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
        val game = makeGameCache()
        val game2 = makeGameCache()
        val company1 = makeCompanyDao()
        val company2 = makeCompanyDao()
        val data = GameDeveloperDao(game.id, company1.id)
        val data2 = GameDeveloperDao(game.id, company2.id)
        val data3 = GameDeveloperDao(game2.id, company1.id)

        database?.gamesDao()?.insert(game)
        database?.gamesDao()?.insert(game2)
        database?.companyDao()?.insert(company1)
        database?.companyDao()?.insert(company2)
        database?.companyDao()?.insertGameDeveloper(data)
        database?.companyDao()?.insertGameDeveloper(data2)
        database?.companyDao()?.insertGameDeveloper(data3)

        // Act
        val observer = database?.companyDao()?.findDeveloperForGame(game.id)?.test()

        assertNotNull(observer)
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
        val game = makeGameCache()
        val game2 = makeGameCache()
        val company1 = makeCompanyDao()
        val company2 = makeCompanyDao()
        val data = GamePublisherDao(game.id, company1.id)
        val data2 = GamePublisherDao(game.id, company2.id)
        val data3 = GamePublisherDao(game2.id, company1.id)

        database?.gamesDao()?.insert(game)
        database?.gamesDao()?.insert(game2)
        database?.companyDao()?.insert(company1)
        database?.companyDao()?.insert(company2)
        database?.companyDao()?.insertGamePublisher(data)
        database?.companyDao()?.insertGamePublisher(data2)
        database?.companyDao()?.insertGamePublisher(data3)

        // Act
        val observer = database?.companyDao()?.findPublishersForGame(game.id)?.test()

        assertNotNull(observer)
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

        val game = makeGameCache()
        database?.gamesDao()?.insert(game)

        // Act
        // Act
        val observer = database?.companyDao()?.findDeveloperForGame(game.id)?.test()

        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue { it.isEmpty() }
        }
    }

    @Test
    fun findPublishersForGameShouldReturnEmptyListWhenNoMatches() {

        val game = makeGameCache()
        database?.gamesDao()?.insert(game)

        // Act
        // Act
        val observer = database?.companyDao()?.findPublishersForGame(game.id)?.test()

        assertNotNull(observer)
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