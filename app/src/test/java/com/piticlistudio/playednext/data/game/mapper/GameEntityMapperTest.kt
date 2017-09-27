package com.piticlistudio.playednext.data.game.mapper

import com.piticlistudio.playednext.data.game.model.GameEntity
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
        @DisplayName("When we call mapFromModel")
        inner class mapFromModel {

            val model = GameEntity(10, "name", "summary", "storyline", 10, 11, 12.0f)
            var result: Game? = null

            @BeforeEach
            fun setup() {
                result = mapper.mapFromModel(model)
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

        @Nested
        @DisplayName("When we call mapFromDomain")
        inner class mapFromDomain {

            val domain = Game(10, "name", "summary", "storyline")
            var result: GameEntity? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDomain(domain)
            }

            @Test
            @DisplayName("Then returns data model")
            fun dataModel() {
                assertNotNull(result)
                with(result) {
                    assertEquals(domain.id, this!!.id)
                    assertEquals(domain.name, name)
                    assertEquals(domain.summary, summary)
                    assertEquals(domain.storyline, storyline)
                }
            }
        }
    }
}