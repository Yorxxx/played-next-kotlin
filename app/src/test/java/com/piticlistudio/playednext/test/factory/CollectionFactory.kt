package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.room.RoomCollection
import com.piticlistudio.playednext.data.entity.igdb.IGDBCollection
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString

class CollectionFactory {

    companion object Factory {

        fun makeRoomCollection(): RoomCollection {
            return RoomCollection(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString())
        }

        fun makeCollection(id: Int = randomInt(), name: String = randomString(), url: String = randomString()): Collection {
            return Collection(id, name, url)
        }

        fun makeIGDBCollection(): IGDBCollection {
            return IGDBCollection(randomInt(), randomString(), randomString(), randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }
    }
}