package com.piticlistudio.playednext.data.game.mapper.remote

import com.piticlistudio.playednext.data.game.model.remote.IGDBGameModel
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test cases for [IGDBGameMapper]
 */
class IGDBGameMapperTest {

    private var mapper = IGDBGameMapper()

    @Test
    fun shouldMapIntoDataModel() {

        val remote = IGDBGameModel(10, "name", "summary", "storyline",
                20, 30, 95, listOf(1, 2, 3), listOf(), listOf(10), 1000)

        val result = mapper.mapFromRemote(remote)

        assertNotNull(result)
        with(result) {
            assertEquals(remote.id, id)
            assertEquals(remote.name, name)
            assertEquals(remote.summary, summary)
            assertEquals(remote.storyline, storyline)
            assertEquals(remote.collection, collection)
            assertEquals(remote.franchise, franchise)
            assertEquals(remote.rating, rating)
        }
    }
}