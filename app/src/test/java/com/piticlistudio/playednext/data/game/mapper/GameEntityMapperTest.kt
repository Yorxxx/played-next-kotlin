package com.piticlistudio.playednext.data.game.mapper

import com.piticlistudio.playednext.data.game.model.GameModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Test cases for [GameEntityMapper]
 */
class GameEntityMapperTest {

    private val mapper = GameEntityMapper()

    @Test
    fun shouldMapIntoDomainModel() {
        val data = GameModel(10, "name", "summary", "storyline", 10, 11, 90)

        val result = mapper.mapFromRemote(data)

        assertNotNull(result)
        with(result) {
            assertEquals(data.id, id)
            assertEquals(data.name, name)
            assertEquals(data.summary, summary)
            assertEquals(data.storyline, storyline)
        }
    }
}