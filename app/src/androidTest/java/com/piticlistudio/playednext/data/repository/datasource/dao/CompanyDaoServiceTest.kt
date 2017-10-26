package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.GameDeveloperDao
import com.piticlistudio.playednext.data.entity.dao.GamePublisherDao
import com.piticlistudio.playednext.test.factory.DomainFactory.Factory.makeCompanyDao
import com.piticlistudio.playednext.test.factory.DomainFactory.Factory.makeGameCache
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
    var instantTaskExecutorRule = InstantTaskExecutorRule();

    private var database: AppDatabase? = null

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @Test
    fun insertCompany_shouldStoreData() {

        val data = makeCompanyDao()

        val result = database?.companyDao()?.insertCompany(data)

        assertNotNull(result)
        assertEquals(data.id, result!!.toInt())
    }

    fun insertCompany_ignoresIfAlreadyStored() {

        val data = makeCompanyDao()

        val id = database?.companyDao()?.insertCompany(data)
        val id2 = database?.companyDao()?.insertCompany(data)

        assertTrue(id!! > 0L)
        assertEquals(0L, id2)
    }

    @Test
    fun insertGameDeveloper_shouldStoreData() {
        val company = makeCompanyDao()
        val game = makeGameCache()
        val data = GameDeveloperDao(game.id, company.id)

        database?.gamesDao()?.insertGame(game)
        database?.companyDao()?.insertCompany(company)
        val result = database?.companyDao()?.insertGameDeveloper(data)

        assertNotNull(result)
        assertTrue(result!! > 0)
    }

    @Test
    fun insertGamePublisher_shouldStoreData() {
        val company = makeCompanyDao()
        val game = makeGameCache()
        val data = GamePublisherDao(game.id, company.id)

        database?.gamesDao()?.insertGame(game)
        database?.companyDao()?.insertCompany(company)
        val result = database?.companyDao()?.insertGamePublisher(data)

        assertNotNull(result)
        assertTrue(result!! > 0)
    }

    @Test
    fun insertGameDeveloper_shouldReplaceDataOnConflict() {
        val company = makeCompanyDao()
        val game = makeGameCache()
        val data = GameDeveloperDao(game.id, company.id)

        database?.gamesDao()?.insertGame(game)
        database?.companyDao()?.insertCompany(company)
        database?.companyDao()?.insertGameDeveloper(data)
        val result = database?.companyDao()?.insertGameDeveloper(data)

        assertNotNull(result)
        assertTrue(result!! > 0)
    }

    @Test
    fun insertGamePublisher_shouldReplaceDataOnConflict() {
        val company = makeCompanyDao()
        val game = makeGameCache()
        val data = GamePublisherDao(game.id, company.id)

        database?.gamesDao()?.insertGame(game)
        database?.companyDao()?.insertCompany(company)
        database?.companyDao()?.insertGamePublisher(data)
        val result = database?.companyDao()?.insertGamePublisher(data)

        assertNotNull(result)
        assertTrue(result!! > 0)
    }

    @Test
    fun findCompanyById_throwsErrorIfNotFound() {
        val observer = database?.companyDao()?.findCompanyById(2)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoValues()
            assertNotComplete()
            assertError { it is EmptyResultSetException }
        }
    }

    @Test
    fun findCompanyById_returnsData() {
        val data = makeCompanyDao()

        database?.companyDao()?.insertCompany(data)

        val observer = database?.companyDao()?.findCompanyById(data.id.toLong())?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue(data)
        }
    }

    @Test
    fun findDeveloperForGame_returnsData() {
        val game = makeGameCache()
        val game2 = makeGameCache()
        val company1 = makeCompanyDao()
        val company2 = makeCompanyDao()
        val data = GameDeveloperDao(game.id, company1.id)
        val data2 = GameDeveloperDao(game.id, company2.id)
        val data3 = GameDeveloperDao(game2.id, company1.id)

        database?.gamesDao()?.insertGame(game)
        database?.gamesDao()?.insertGame(game2)
        database?.companyDao()?.insertCompany(company1)
        database?.companyDao()?.insertCompany(company2)
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
    fun findPublishersForGame_returnsData() {
        val game = makeGameCache()
        val game2 = makeGameCache()
        val company1 = makeCompanyDao()
        val company2 = makeCompanyDao()
        val data = GamePublisherDao(game.id, company1.id)
        val data2 = GamePublisherDao(game.id, company2.id)
        val data3 = GamePublisherDao(game2.id, company1.id)

        database?.gamesDao()?.insertGame(game)
        database?.gamesDao()?.insertGame(game2)
        database?.companyDao()?.insertCompany(company1)
        database?.companyDao()?.insertCompany(company2)
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
    fun findDeveloperForGame_returnsEmptyList() {

        val game = makeGameCache()
        database?.gamesDao()?.insertGame(game)

        // Act
        // Act
        val observer = database?.companyDao()?.findDeveloperForGame(game.id)?.test()

        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue { it.size == 0 }
        }
    }

    @Test
    fun findPublishersForGame_returnsEmptyList() {

        val game = makeGameCache()
        database?.gamesDao()?.insertGame(game)

        // Act
        // Act
        val observer = database?.companyDao()?.findPublishersForGame(game.id)?.test()

        assertNotNull(observer)
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