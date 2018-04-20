package com.piticlistudio.playednext.data.repository.datasource.room

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.entity.room.RoomGameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.factory.GameRelationFactory.Factory.makeRoomGameRelation
import junit.framework.Assert
import junit.framework.Assert.fail
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomRelationServiceTest : BaseRoomServiceTest() {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule();

    @Test
    fun insert_shouldStore() {

        val data = makeRoomGameRelation(getRandomStoredGameId(), getRandomStoredPlatformId())

        val result = database.relationDao().insert(data)

        Assert.assertNotNull(result)
    }

    @Test
    fun insert_failsIfGameIsNotStored() {

        val notStoredGameId = getStoredGameIds().min()?.minus(1)

        try {
            val data = makeRoomGameRelation(notStoredGameId!!, getRandomStoredPlatformId())
            database.relationDao().insert(data)
            fail("should have thrown")
        } catch (e: Throwable) {
        }
    }

    @Test
    fun insert_failsIfPlatformIsNotStored() {

        val notStoredPlatformId = getStoredPlatformIds().min()?.minus(1)

        try {
            val data = makeRoomGameRelation(getRandomStoredGameId(), notStoredPlatformId!!)
            database.relationDao().insert(data)
            fail("should have thrown")
        } catch (e: Throwable) {
        }
    }

    @Test
    fun findForGameAndPlatform_shouldReturnRelation() {

        val gameId = storedRelation?.gameId
        val platformId = storedRelation?.platformId

        requireNotNull(gameId)
        requireNotNull(platformId)

        // Act
        val observer = database.relationDao().findForGameAndPlatform(gameId!!, platformId!!).test()
        Assert.assertNotNull(observer)
        observer?.apply {
            assertValueCount(1)
            assertNotComplete()
            assertNoErrors()
            assertValue { it.isNotEmpty() }
        }
    }

    @Test
    fun findWithStatus_returnsListWithRelations() {

        val result = database.relationDao().findWithStatus(GameRelationStatus.PLAYING.ordinal).test()

        with(result!!) {
            assertValueCount(1)
            assertNotComplete()
            assertNoErrors()
            assertValue { it.isNotEmpty() }
            assertValue {
                it.find { !it.status.equals(GameRelationStatus.PLAYING.ordinal) } == null
            }
        }
    }

    @After
    fun tearDown() {
        database.close()
    }
}