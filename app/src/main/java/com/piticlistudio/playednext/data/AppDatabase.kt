package com.piticlistudio.playednext.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.piticlistudio.playednext.data.entity.dao.*
import com.piticlistudio.playednext.data.repository.datasource.dao.CollectionDaoService
import com.piticlistudio.playednext.data.repository.datasource.dao.CompanyDaoService
import com.piticlistudio.playednext.data.repository.datasource.dao.GameDaoService
import com.piticlistudio.playednext.data.repository.datasource.dao.GenreDaoService
import com.piticlistudio.playednext.data.repository.datasource.dao.platform.PlatformDaoService

@Database(entities = arrayOf(GameDao::class, CompanyDao::class, GameDeveloperDao::class,
        GamePublisherDao::class, GenreDao::class, GameGenreDao::class, CollectionDao::class,
        GameCollectionDao::class, PlatformDao::class, GamePlatformDao::class),
        version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gamesDao(): GameDaoService

    abstract fun companyDao(): CompanyDaoService

    abstract fun genreDao(): GenreDaoService

    abstract fun collectionDao(): CollectionDaoService

    abstract fun platformDao(): PlatformDaoService
}