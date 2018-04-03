package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.room.CollectionDao
import com.piticlistudio.playednext.data.entity.igdb.CollectionDTO
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString

class CollectionFactory {

    companion object Factory {

        fun makeCollectionDao(): CollectionDao {
            return CollectionDao(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString())
        }

        fun makeCollection(id: Int = randomInt(), name: String = randomString(), url: String = randomString()): Collection {
            return Collection(id, name, url)
        }

        fun makeCollectionDTO(): CollectionDTO {
            return CollectionDTO(randomInt(), randomString(), randomString(), randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }
    }
}