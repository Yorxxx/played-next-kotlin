package com.piticlistudio.playednext.data.repository.datasource.room.playlist

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.database.sqlite.SQLiteConstraintException
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.entity.room.RoomPlaylistEntity
import com.piticlistudio.playednext.data.entity.room.RoomPlaylistGameRelationEntity
import com.piticlistudio.playednext.data.repository.datasource.room.BaseRoomServiceTest
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomString
import com.piticlistudio.playednext.factory.PlaylistFactory.Factory.makeRoomPlaylist
import junit.framework.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomPlaylistServiceTest : BaseRoomServiceTest() {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertShouldStoreData() {

        val data = makeRoomPlaylist()

        val result = database.playlistRoom().insert(data)

        assertNotNull(result)
        assertTrue(result > 0)
    }

    @Test
    fun insertShouldReplaceExistingData() {
        val data = makeRoomPlaylist(getRandomStoredPlaylistName())

        val result = database.playlistRoom().insert(data)
        assertNotNull(result)
        assertTrue(result > 0)
    }

    @Test
    fun shouldDeleteData() {

        requireNotNull(storedPlaylist)
        val result = database.playlistRoom().delete(storedPlaylist!!)
        assertEquals(1, result)
    }

    @Test
    fun requestingToDeleteANonStoredDataReturnsZero() {

        val nonExistingData = makeRoomPlaylist("foo")
        val result = database.playlistRoom().delete(nonExistingData)
        assertEquals(0, result)
    }

    @Test
    fun shouldReturnAllPlaylists() {

        val observer = database.playlistRoom().findAll().test()

        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertNotComplete()
            assertValueCount(1)
            assertValue { it.size == getStoredPlaylistNames().size }
        }
    }

    @Test
    fun shouldEmitAllPlaylistsWhenANewOneIsInserted() {

        val observer = database.playlistRoom().findAll().test()
        observer?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertValue { it.size == getStoredPlaylistNames().size }
        }

        database.playlistRoom().insert(makeRoomPlaylist())

        observer?.apply {
            assertNoErrors()
            assertValueCount(2)
            assertValueAt(1, { it.size == getStoredPlaylistNames().size + 1 })
        }
    }

    @Test
    fun shouldEmitStoredPlaylistName() {

        val requestedName = getRandomStoredPlaylistName()
        val observer = database.playlistRoom().find(requestedName).test()

        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertNotComplete()
            assertValueCount(1)
            assertValue { it.size == 1 && it.first().name == requestedName }
        }
    }

    @Test
    fun shouldEmitEmptyListWhenNoMatches() {

        val observer = database.playlistRoom().find("foo").test()

        assertNotNull(observer)
        observer?.apply {
            assertNoErrors()
            assertNotComplete()
            assertValueCount(1)
            assertValue { it.isEmpty() }
        }
    }

    @Test
    fun shouldEmitAgainWhenPlaylistIsUpdated() {

        val requestedName = getRandomStoredPlaylistName()
        val observer = database.playlistRoom().find(requestedName).test()

        observer?.apply {
            assertNoErrors()
            assertNotComplete()
            assertValueCount(1)
            assertValue { it.size == 1 && it.first().name == requestedName }
        }

        val updatedPlaylist = makeRoomPlaylist(requestedName)
        database.playlistRoom().insert(updatedPlaylist)

        // Assert
        observer?.apply {
            assertNoErrors()
            assertNotComplete()
            assertValueCount(2)
        }
    }
}