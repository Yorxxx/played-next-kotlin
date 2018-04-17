package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.igdb.IGDBTimeToBeat
import com.piticlistudio.playednext.data.entity.room.RoomTimeToBeat
import com.piticlistudio.playednext.domain.model.TimeToBeat
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt

class TimeToBeatFactory {

    companion object Factory {

        fun makeTimeToBeat(): TimeToBeat = TimeToBeat(DataFactory.randomInt(), DataFactory.randomInt(), DataFactory.randomInt())

        fun makeIGDBTimeToBeat(): IGDBTimeToBeat = IGDBTimeToBeat(randomInt(), randomInt(), randomInt())

        fun makeRoomTimeToBeat(): RoomTimeToBeat = RoomTimeToBeat(randomInt(), randomInt(), randomInt())
    }
}