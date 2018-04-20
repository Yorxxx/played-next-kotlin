package com.piticlistudio.playednext.data.repository.datasource.room.playlist

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.database.sqlite.SQLiteConstraintException
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.repository.datasource.room.BaseRoomServiceTest
import com.piticlistudio.playednext.factory.PlatformFactory
import com.piticlistudio.playednext.factory.PlaylistFactory.Factory.makeRoomPlaylist
import junit.framework.Assert
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
    fun insertShouldAbortIfAPlaylistWithThatNameAlreadyExists() {
        val data = makeRoomPlaylist(getRandomStoredPlaylistName())

        try {
            database.playlistRoom().insert(data)
            fail("should have failed")
        } catch (e: SQLiteConstraintException) {

        }
    }

    @Test
    fun shouldUpdateData() {
        val data = makeRoomPlaylist(getRandomStoredPlaylistName())

        try {
            val result = database.playlistRoom().update(data)
            assertTrue(result > 0)
        } catch (e: SQLiteConstraintException) {
            fail(e.message)
        }
    }

    @Test
    fun shouldThrowIfNoDataToUpdate() {
        val data = makeRoomPlaylist("abc")

        try {
            database.playlistRoom().update(data)
            fail("should have failed")
        } catch (e: SQLiteConstraintException) {

        }
    }

    @Test
    fun shouldDeleteData() {

        requireNotNull(storedPlaylist)
        val result = database.playlistRoom().delete(storedPlaylist!!)
        assertEquals(1, result)
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

        database.playlistRoom().findAll().test()?.apply {
            assertNoErrors()
            assertValueCount(1)
            assertValue { it.size == getStoredPlaylistNames().size }
        }

        database.playlistRoom().insert(makeRoomPlaylist())


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
            assertValue { it.name == requestedName }
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
            assertValue { it.name == requestedName }
        }

        val updatedPlaylist = makeRoomPlaylist(requestedName)
        database.playlistRoom().update(updatedPlaylist)

        // Assert
        observer?.apply {
            assertNoErrors()
            assertNotComplete()
            assertValueCount(2)
            assertValue { it.name == requestedName }
        }
    }
}