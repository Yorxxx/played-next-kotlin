package com.piticlistudio.playednext.data.repository.datasource.room.company

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.EmptyResultSetException
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.entity.room.RoomGameDeveloper
import com.piticlistudio.playednext.data.entity.room.RoomGamePublisher
import com.piticlistudio.playednext.data.repository.datasource.room.BaseRoomServiceTest
import com.piticlistudio.playednext.factory.CompanyFactory.Factory.makeCompanyRoom
import junit.framework.Assert
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class RoomCompanyServiceTest : BaseRoomServiceTest() {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertShouldStoreData() {

        val data = makeCompanyRoom()

        val result = database.companyRoom().insert(data)

        Assert.assertNotNull(result)
        Assert.assertEquals(data.id, result.toInt())
    }

    @Test
    fun insertGameDeveloperShouldStoreData() {

        val data = RoomGameDeveloper(getRandomStoredGameId(), getRandomStoredCompanyId())

        val result = database.companyRoom().insertGameDeveloper(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result > 0)
    }

    @Test
    fun insertGamePublisherShouldStoreData() {
        val data = RoomGamePublisher(getRandomStoredGameId(), getRandomStoredCompanyId())

        val result = database.companyRoom().insertGamePublisher(data)

        Assert.assertNotNull(result)
        Assert.assertTrue(result > 0)
    }

    @Test
    fun findShouldThrowErrorIfNotFound() {
        val id = getStoredCompanyIds().min()?.minus(1)

        val observer = database.companyRoom().find(id!!.toLong()).test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoValues()
            assertNotComplete()
            assertError { it is EmptyResultSetException }
        }
    }

    @Test
    fun findShouldReturnData() {
        val existingId = getRandomStoredCompanyId()

        val observer = database.companyRoom().find(existingId.toLong()).test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertComplete()
            assertValue { it.id == existingId }
        }
    }

    @Test
    fun findDeveloperForGameShouldReturnData() {
        val gameId = getRandomStoredGameId()

        // Act
        val observer = database.companyRoom().findDeveloperForGame(gameId).test()

        Assert.assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertNotComplete()
            assertValue { it.isNotEmpty() }
        }
    }

    @Test
    fun findPublishersForGameShouldReturnData() {
        val gameId = getRandomStoredGameId()

        // Act
        val observer = database.companyRoom().findPublishersForGame(gameId).test()

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