package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.dao.CollectionDao
import com.piticlistudio.playednext.data.entity.net.CollectionDTO
import com.piticlistudio.playednext.domain.model.Collection

class CollectionFactory {

    companion object Factory {

        fun makeCollectionDao(): CollectionDao {
            return CollectionDao(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makeCollection(): Collection {
            return Collection(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makeCollectionDTO(): CollectionDTO {
            return CollectionDTO(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }
    }
}