package com.piticlistudio.playednext.data.entity.mapper

import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GameEntityToDomainMapperTest {

    @Nested
    @DisplayName("Given GameEntityToDomainMapper instance")
    inner class GameEntityToDomainMapperInstance {

        private lateinit var mapper: GameEntityToDomainMapper

        @BeforeEach
        internal fun setUp() {
            mapper = GameEntityToDomainMapper()
        }

        @Nested
        @DisplayName("When mapFromModel is called")
        inner class mapFromModelCalled {

            private val model = makeGameEntity()
            private var response: Game? = null

            @BeforeEach
            internal fun setUp() {
                response = mapper.mapFromModel(model)
            }

            @Test
            @DisplayName("Then should map into domain model")
            fun isMapped() {
                assertNotNull(response)
                response?.apply {
                    assertEquals(model.id, id)
                    assertEquals(model.name, name)
                    assertEquals(model.storyline, storyline)
                    assertEquals(model.summary, summary)
                }
            }
        }
    }
}