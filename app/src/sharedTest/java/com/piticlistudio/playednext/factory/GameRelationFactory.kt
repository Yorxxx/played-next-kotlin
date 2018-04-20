package com.piticlistudio.playednext.factory

import com.piticlistudio.playednext.data.entity.room.RoomGameRelation
import com.piticlistudio.playednext.data.entity.room.RoomGameRelationProxy
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeRoomGameProxy
import com.piticlistudio.playednext.factory.PlatformFactory.Factory.makePlatform
import com.piticlistudio.playednext.factory.PlatformFactory.Factory.makeRoomPlatform

class GameRelationFactory {

    companion object Factory {

        fun makeRoomGameRelation(gameId: Int = randomInt(),
                                 platformId: Int = randomInt()): RoomGameRelation {
            return RoomGameRelation(gameId, platformId, makeRelationStatus().ordinal, randomLong(), randomLong())
        }

        fun makeGameRelation(status: GameRelationStatus = makeRelationStatus()): GameRelation {
            return GameRelation(makeGame(), makePlatform(), status, randomLong(), randomLong())
        }

        fun makeRelationStatus(): GameRelationStatus {
            return GameRelationStatus.values().get(randomInt() % GameRelationStatus.values().size)
        }

        fun makeRoomGameRelationProxy(): RoomGameRelationProxy {
            return RoomGameRelationProxy(game = makeRoomGameProxy(),
                    relation = makeRoomGameRelation(),
                    platform = makeRoomPlatform())
        }
    }
}