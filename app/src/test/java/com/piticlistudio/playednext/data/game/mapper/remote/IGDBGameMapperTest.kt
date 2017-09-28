package com.piticlistudio.playednext.data.game.mapper.remote

import com.piticlistudio.playednext.data.entity.GameEntity
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameRemoteMapper
import com.piticlistudio.playednext.data.entity.net.GameRemote
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test cases for [IGDBGameMapper]
 */
class IGDBGameMapperTest {

    /*@Nested
    @DisplayName("Given a IGDBGameMapper instance")
    inner class IGDBGameMapperInstance {

        val mapper = GameRemoteMapper()

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class mapFromModel {

            val model = GameRemote(10, "name", "summary", "storyline",
                    20, 30)
            var result: GameEntity? = null

            @BeforeEach
            fun setup() {
                result = mapper.mapFromModel(model)
            }

            @Test
            @DisplayName("Then returns a data model")
            fun domainModelNotNull() {
                assertNotNull(result)
            }

            @Test
            @DisplayName("Then maps values into model")
            fun valuesAreMapped() {
                with(result) {
                    assertEquals(model.id, this!!.id)
                    assertEquals(model.name, name)
                    assertEquals(model.summary, summary)
                    assertEquals(model.storyline, storyline)
                    assertEquals(model.collection, collection)
                    assertEquals(model.franchise, franchise)
                    assertEquals(model.rating, rating)
                }
            }

            @Nested
            @DisplayName("And has cover")
            inner class hasCover {

//                @Test
//                @DisplayName("Then maps cover")
            }
        }
    }*/
}