package com.piticlistudio.playednext.data.repository.datasource.room

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeGameCache
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeRoomPlatform
import com.piticlistudio.playednext.factory.DomainFactory.Factory.makeRelationDao
import junit.framework.Assert
import junit.framework.Assert.fail
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RelationDaoServiceTest {

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
    fun insert_shouldStore() {

        val game = makeGameCache()
        val platform = makeRoomPlatform()
        database?.gamesDao()?.insert(game)
        database?.platformRoom()?.insert(platform)

        val data = makeRelationDao(game.id, platform.id)

        val result = database?.relationDao()?.insert(data)

        Assert.assertNotNull(result)
    }

    @Test
    fun insert_failsIfGameIsNotStored() {

        val game = makeGameCache()
        val platform = makeRoomPlatform()
        database?.platformRoom()?.insert(platform)

        try {
            val data = makeRelationDao(game.id, platform.id)
            database?.relationDao()?.insert(data)
            fail("should have thrown")
        } catch (e: Throwable) {
        }
    }

    @Test
    fun insert_failsIfPlatformIsNotStored() {

        val game = makeGameCache()
        val platform = makeRoomPlatform()
        database?.gamesDao()?.insert(game)

        try {
            val data = makeRelationDao(game.id, platform.id)
            database?.relationDao()?.insert(data)
            fail("should have thrown")
        } catch (e: Throwable) {
        }
    }

    @Test
    fun findForGameAndPlatform_shouldReturnRelation() {
        val game = makeGameCache()
        val platform = makeRoomPlatform()
        database?.gamesDao()?.insert(game)
        database?.platformRoom()?.insert(platform)
        val relation = makeRelationDao(game.id, platform.id)
        database?.relationDao()?.insert(relation)

        // Act
        val observer = database?.relationDao()?.findForGameAndPlatform(game.id, platform.id)?.test()
        Assert.assertNotNull(observer)
        observer?.apply {
            assertValueCount(1)
            assertNotComplete()
            assertNoErrors()
            assertValue { it.contains(relation) && it.size == 1 }
        }
    }

    @Test
    fun findForGameAndPlatform_returnsEmptyListIfNoMatch() {
        val game = makeGameCache()
        val platform = makeRoomPlatform()
        database?.gamesDao()?.insert(game)
        database?.platformRoom()?.insert(platform)
        val relation = makeRelationDao(game.id, platform.id)
        database?.relationDao()?.insert(relation)

        // Act
        val observer = database?.relationDao()?.findForGameAndPlatform(game.id, platform.id + 1)?.test()
        Assert.assertNotNull(observer)
        observer?.apply {
            assertValueCount(1)
            assertNotComplete()
            assertNoErrors()
            assertValue { it.isEmpty() }
        }
    }

    @After
    fun tearDown() {
        database?.close()
    }
}