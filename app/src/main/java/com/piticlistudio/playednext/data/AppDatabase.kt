package com.piticlistudio.playednext.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.piticlistudio.playednext.data.entity.room.*
import com.piticlistudio.playednext.data.repository.datasource.room.franchise.RoomCollectionService
import com.piticlistudio.playednext.data.repository.datasource.room.company.RoomCompanyService
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameService
import com.piticlistudio.playednext.data.repository.datasource.room.gameplaylist.RoomGamePlaylistService
import com.piticlistudio.playednext.data.repository.datasource.room.genre.RoomGenreService
import com.piticlistudio.playednext.data.repository.datasource.room.image.RoomGameImagesService
import com.piticlistudio.playednext.data.repository.datasource.room.platform.RoomGamePlatformService
import com.piticlistudio.playednext.data.repository.datasource.room.playlist.RoomPlaylistService
import com.piticlistudio.playednext.data.repository.datasource.room.relation.RoomRelationService

@Database(entities = arrayOf(RoomGame::class, RoomCompany::class, RoomGameDeveloper::class,
        RoomGamePublisher::class, RoomGenre::class, RoomGameGenre::class, RoomCollection::class,
        RoomGameCollection::class, RoomPlatform::class, RoomGamePlatform::class, RoomGameRelation::class,
        RoomGameImage::class, RoomPlaylistEntity::class, RoomPlaylistGameRelationEntity::class),
        version = 13, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gamesDao(): RoomGameService

    abstract fun playlistRoom(): RoomPlaylistService

    abstract fun companyRoom(): RoomCompanyService

    abstract fun genreRoom(): RoomGenreService

    abstract fun collectionRoom(): RoomCollectionService

    abstract fun platformRoom(): RoomGamePlatformService

    abstract fun relationDao(): RoomRelationService

    abstract fun imageRoom(): RoomGameImagesService

    abstract fun gamePlaylistRoom(): RoomGamePlaylistService

}