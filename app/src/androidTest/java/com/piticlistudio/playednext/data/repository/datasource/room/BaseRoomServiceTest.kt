package com.piticlistudio.playednext.data.repository.datasource.room

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.room.*
import com.piticlistudio.playednext.factory.CompanyFactory
import com.piticlistudio.playednext.factory.DataFactory
import com.piticlistudio.playednext.factory.GameImageFactory
import com.piticlistudio.playednext.factory.GameRelationFactory
import com.piticlistudio.playednext.test.factory.*
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makeRoomPlatform
import org.junit.Before
import java.util.*

/**
 * Base abstract class for setting up a populated database with ROOM
 */
abstract class BaseRoomServiceTest {

    protected lateinit var database: AppDatabase

    private val storedCompanyIds = mutableSetOf<Int>()
    protected fun getStoredCompanyIds(): Set<Int> = storedCompanyIds
    protected fun getRandomStoredCompanyId(): Int = storedCompanyIds.toList().random() ?: 0

    private val storedGameIds = mutableSetOf<Int>()
    protected fun getStoredGameIds(): Set<Int> = storedGameIds
    protected fun getRandomStoredGameId(): Int = storedGameIds.toList().random() ?: 0

    private val storedCollectionIds = mutableSetOf<Int>()
    protected fun getStoredCollectionIds(): Set<Int> = storedCollectionIds
    protected fun getRandomStoredCollectionId(): Int = storedCollectionIds.toList().random() ?: 0

    private val storedImageIds = mutableSetOf<Int>()

    private val storedGenreIds = mutableSetOf<Int>()
    protected fun getStoredGenreIds(): Set<Int> = storedGenreIds
    protected fun getRandomStoredGenreId(): Int = storedGenreIds.toList().random() ?: 0

    private val storedPlatformIds = mutableSetOf<Int>()
    protected fun getStoredPlatformIds(): Set<Int> = storedPlatformIds
    protected fun getRandomStoredPlatformId(): Int = storedPlatformIds.toList().random() ?: 0

    protected var storedRelation: RoomGameRelation? = null

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        setupDatabase()
    }

    private fun setupDatabase() {
        val count = DataFactory.randomInt(50)
        storedCompanyIds.clear()
        storedGameIds.clear()
        storedCollectionIds.clear()
        storedImageIds.clear()
        storedGenreIds.clear()
        storedPlatformIds.clear()

        repeat(count) {
            val game = GameFactory.makeRoomGame(id = it)
            database.gamesDao().insert(game).apply {
                storedGameIds.add(game.id)
            }

            populateDatabaseWithPlatformsForGameWithId(game.id)
            populateDatabaseWithCompaniesForGameWithId(game.id)
            populateDatabaseWithFranchisesForGameWithId(game.id)
            populateDatabaseWithImagesForGameWithId(game.id)
            populateDatabaseWithGenresGameWithId(game.id)
            populateDatabaseWithRelationForGameWithId(game.id)
        }
    }

    private fun populateDatabaseWithCompaniesForGameWithId(gameId: Int) {
        val company = CompanyFactory.makeCompanyRoom()
        database.companyRoom().insert(company).apply {
            storedCompanyIds.add(company.id)
        }

        val innerCount = DataFactory.randomInt(5)
        repeat(innerCount) {
            database.companyRoom().insertGameDeveloper(RoomGameDeveloper(gameId, company.id))
        }
        repeat(innerCount) {
            database.companyRoom().insertGamePublisher(RoomGamePublisher(gameId, company.id))
        }
    }

    private fun populateDatabaseWithFranchisesForGameWithId(gameId: Int) {
        val collection = CollectionFactory.makeRoomCollection()
        database.collectionRoom().insert(collection).apply {
            storedCollectionIds.add(collection.id)
        }

        val innerCount = DataFactory.randomInt(5)
        repeat(innerCount) {
            database.collectionRoom().insertGameCollection(RoomGameCollection(gameId, collection.id))
        }
    }

    private fun populateDatabaseWithGenresGameWithId(gameId: Int) {
        val data = GenreFactory.makeRoomGenre()
        database.genreRoom().insert(data).apply {
            storedGenreIds.add(data.id)

            val innerCount = DataFactory.randomInt(5)
            repeat(innerCount) {
                database.genreRoom().insertGameGenre(RoomGameGenre(gameId, data.id))
            }
        }
    }

    private fun populateDatabaseWithImagesForGameWithId(gameId: Int) {
        val data = GameImageFactory.makeRoomGameImage(gameId = gameId)
        database.imageRoom().insert(data).apply {
            storedImageIds.add(this.toInt())
        }
    }

    private fun populateDatabaseWithPlatformsForGameWithId(gameId: Int) {
        val data = makeRoomPlatform()
        database.platformRoom().insert(data).apply {
            storedPlatformIds.add(data.id)
        }
        val innerCount = DataFactory.randomInt(5)
        repeat(innerCount) {
            database.platformRoom().insertGamePlatform(RoomGamePlatform(gameId, data.id))
        }
    }

    private fun populateDatabaseWithRelationForGameWithId(gameId: Int) {
        storedRelation = GameRelationFactory.makeRoomGameRelation(gameId, getRandomStoredPlatformId())
        database.relationDao().insert(storedRelation!!)
    }
}

/**
 * Returns a random element.
 */
fun <E> List<E>.random(): E? = if (size > 0) get(Random().nextInt(size)) else null