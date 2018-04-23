package com.piticlistudio.playednext.data.repository.datasource.room.gameplaylist

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.database.sqlite.SQLiteConstraintException
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.entity.room.RoomPlaylistGameRelationEntity
import com.piticlistudio.playednext.data.repository.datasource.room.BaseRoomServiceTest
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomInt
import junit.framework.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomGamePlaylistServiceTest : BaseRoomServiceTest() {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertShouldStoreData() {

        val data = RoomPlaylistGameRelationEntity(getRandomStoredPlaylistName(), getRandomStoredGameId())

        val result = database.gamePlaylistRoom().insert(data)

        assertNotNull(result)
        assertTrue(result > 0)
    }

    @Test
    fun insertShouldReplaceExistingData() {

        val data = RoomPlaylistGameRelationEntity(getRandomStoredPlaylistName(), getRandomStoredGameId())

        var result = database.gamePlaylistRoom().insert(data)
        assertNotNull(result)
        assertTrue(result > 0)

        val data2 = RoomPlaylistGameRelationEntity(data.playlistName, data.gameId)
        result = database.gamePlaylistRoom().insert(data2)
        assertNotNull(result)
        assertTrue(result > 0)
    }

    @Test
    fun insertShouldFailIfPlaylistIsNotStoredPreviously() {

        val data = RoomPlaylistGameRelationEntity("foo", getRandomStoredGameId())

        try {
            database.gamePlaylistRoom().insert(data)
            fail("should have failed")
        } catch (e: SQLiteConstraintException) {

        }
    }

    @Test
    fun insertShouldFailIfGameIsNotStoredPreviously() {

        val gameId = getStoredGameIds().min()?.minus(1)
        val data = RoomPlaylistGameRelationEntity(getRandomStoredPlaylistName(), gameId!!)

        try {
            database.gamePlaylistRoom().insert(data)
            fail("should have failed")
        } catch (e: SQLiteConstraintException) {

        }
    }

    @Test
    fun findShouldReturnAllItemsGivenAPlaylist() {

        val playlistName = getRandomStoredPlaylistName()
        val gamesInPlaylist = 10
        getStoredGameIds().take(gamesInPlaylist).forEach {
            val data = RoomPlaylistGameRelationEntity(playlistName, it)
            if (database.gamePlaylistRoom().insert(data) == 0L) {
                fail("Failed arranging test")
            }
        }

        val observer = database.gamePlaylistRoom().find(playlistName).test()

        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertNotComplete()
            assertValueCount(1)
            assertValue { it.size == gamesInPlaylist }
        }
    }

    @Test
    fun deletingAPlaylistShouldDeleteAllItems() {

        val gamesInPlaylist = 10
        getStoredGameIds().take(gamesInPlaylist).forEach {
            val data = RoomPlaylistGameRelationEntity(storedPlaylist!!.name, it)
            if (database.gamePlaylistRoom().insert(data) == 0L) {
                fail("Failed arranging test")
            }
        }

        val observer = database.gamePlaylistRoom().find(storedPlaylist!!.name).test()
                .assertValue { it.size == gamesInPlaylist }


        // Act
        database.playlistRoom().delete(storedPlaylist!!)

        observer.assertValueCount(2)
        observer.assertValueAt(1, { it.isEmpty() })
    }
}