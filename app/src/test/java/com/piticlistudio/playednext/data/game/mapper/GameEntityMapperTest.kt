package com.piticlistudio.playednext.data.game.mapper

import com.piticlistudio.playednext.data.game.model.GameModel
import com.piticlistudio.playednext.domain.model.game.Game
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GameEntityMapperTest {

    @Nested
    @DisplayName("Given a GameEntityMapper instance")
    inner class GameEntityMapperInstance {

        val mapper = GameEntityMapper()

        @Nested
        @DisplayName("When we call mapFromRemote")
        inner class mapFromRemote {

            val model = GameModel(10, "name", "summary", "storyline", 10, 11, 12)
            var result: Game? = null

            @BeforeEach
            fun setup() {
                result = mapper.mapFromRemote(model)
            }

            @Test
            @DisplayName("Then returns a domain model")
            fun domainModelNotNull() {
                assertNotNull(result)
            }

            @Test
            @DisplayName("Then maps values into domain model")
            fun valuesAreMapped() {
                with(result) {
                    assertEquals(model.id, this?.id)
                    assertEquals(model.name, this?.name)
                    assertEquals(model.summary, this?.summary)
                    assertEquals(model.storyline, this?.storyline)
                }
            }
        }
    }
}