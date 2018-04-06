package com.piticlistudio.playednext.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.piticlistudio.playednext.data.entity.room.*
import com.piticlistudio.playednext.data.repository.datasource.room.franchise.RoomCollectionService
import com.piticlistudio.playednext.data.repository.datasource.room.company.RoomCompanyService
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameService
import com.piticlistudio.playednext.data.repository.datasource.room.genre.RoomGenreService
import com.piticlistudio.playednext.data.repository.datasource.room.image.RoomGameImagesService
import com.piticlistudio.playednext.data.repository.datasource.room.platform.RoomGamePlatformService
import com.piticlistudio.playednext.data.repository.datasource.room.relation.RelationDaoService

@Database(entities = arrayOf(RoomGame::class, RoomCompany::class, RoomGameDeveloper::class,
        RoomGamePublisher::class, RoomGenre::class, RoomGameGenre::class, RoomCollection::class,
        RoomGameCollection::class, RoomPlatform::class, RoomGamePlatform::class, GameRelationDao::class,
        RoomGameImage::class),
        version = 9, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gamesDao(): RoomGameService

    abstract fun companyRoom(): RoomCompanyService

    abstract fun genreRoom(): RoomGenreService

    abstract fun collectionRoom(): RoomCollectionService

    abstract fun platformRoom(): RoomGamePlatformService

    abstract fun relationDao(): RelationDaoService

    abstract fun imageRoom(): RoomGameImagesService
}