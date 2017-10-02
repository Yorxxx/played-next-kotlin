package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.GameDeveloperDao
import com.piticlistudio.playednext.test.factory.DomainFactory.Factory.makeCompanyDao
import com.piticlistudio.playednext.test.factory.DomainFactory.Factory.makeGameCache
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
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
    fun insertCompanyShouldStoreData() {

        val data = makeCompanyDao()

        val result = database?.companyDao()?.insertCompany(data)

        assertNotNull(result)
        assertEquals(data.id, result!!.toInt())
    }

    @Test
    fun insertGameDeveloper() {
        val company = makeCompanyDao()
        val game = makeGameCache()
        val data = GameDeveloperDao(1, game.id, company.id)

        database?.gamesDao()?.insertGame(game)
        database?.companyDao()?.insertCompany(company)
        val result = database?.companyDao()?.insertGameDeveloper(data)

        assertNotNull(result)
        assertEquals(data.id, result!!.toInt())
    }

    @Test
    fun findCompanyByIdThrowsErrorIfNotFound() {
        val observer = database?.companyDao()?.findCompanyById(2)?.test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoValues()
            assertNotComplete()
            assertError { it is EmptyResultSetException }
        }
    }

    @Test
    fun findCompanyByIdReturnsDataIfPresent() {
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
    fun findDeveloperForGameReturnsData() {
        val game = makeGameCache()
        val company1 = makeCompanyDao()
        val company2 = makeCompanyDao()
        val data = GameDeveloperDao(1, game.id, company1.id)
        val data2 = GameDeveloperDao(2, game.id, company2.id)

        database?.gamesDao()?.insertGame(game)
        database?.companyDao()?.insertCompany(company1)
        database?.companyDao()?.insertCompany(company2)
        database?.companyDao()?.insertGameDeveloper(data)
        database?.companyDao()?.insertGameDeveloper(data2)

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
    fun findDeveloperForGameReturnsEmptyList() {

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

    @After
    fun tearDown() {
        database?.close()
    }
}