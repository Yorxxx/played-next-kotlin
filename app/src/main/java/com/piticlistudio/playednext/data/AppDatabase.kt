package com.piticlistudio.playednext.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.piticlistudio.playednext.data.entity.room.*
import com.piticlistudio.playednext.data.repository.datasource.room.CollectionDaoService
import com.piticlistudio.playednext.data.repository.datasource.room.company.RoomCompanyService
import com.piticlistudio.playednext.data.repository.datasource.room.game.GameDaoService
import com.piticlistudio.playednext.data.repository.datasource.room.GenreDaoService
import com.piticlistudio.playednext.data.repository.datasource.room.image.GameImagesDaoService
import com.piticlistudio.playednext.data.repository.datasource.room.platform.PlatformDaoService
import com.piticlistudio.playednext.data.repository.datasource.room.relation.RelationDaoService

@Database(entities = arrayOf(GameDao::class, RoomCompany::class, RoomGameDeveloper::class,
        RoomGamePublisher::class, GenreDao::class, GameGenreDao::class, CollectionDao::class,
        GameCollectionDao::class, PlatformDao::class, GamePlatformDao::class, GameRelationDao::class,
        ScreenshotDao::class),
        version = 9, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gamesDao(): GameDaoService

    abstract fun companyRoom(): RoomCompanyService

    abstract fun genreDao(): GenreDaoService

    abstract fun collectionDao(): CollectionDaoService

    abstract fun platformDao(): PlatformDaoService

    abstract fun relationDao(): RelationDaoService

    abstract fun imagesDao(): GameImagesDaoService
}