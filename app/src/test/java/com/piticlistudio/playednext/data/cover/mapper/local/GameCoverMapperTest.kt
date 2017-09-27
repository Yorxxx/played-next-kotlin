package com.piticlistudio.playednext.data.cover.mapper.local

import com.piticlistudio.playednext.data.cover.model.CoverEntity
import com.piticlistudio.playednext.data.cover.model.local.GameCover
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GameCoverMapperTest {

    @Nested
    @DisplayName("Given a GameCoverMapper instance")
    inner class GameCoverMapperInstance {

        private lateinit var mapper: GameCoverMapper

        @BeforeEach
        internal fun setUp() {
            mapper = GameCoverMapper()
        }

        @Nested
        @DisplayName("When mapFromModel is called")
        inner class mapFromModel {

            private val model = GameCover("url", 10, 100, 200)
            private var result: CoverEntity? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromModel(model)
            }

            @Test
            @DisplayName("Then should never return null")
            fun neverNull() {
                assertNotNull(result)
            }

            @Test
            @DisplayName("Then should return a CoverEntity")
            fun mapIsCorrect() {
                result?.apply {
                    assertEquals(model.url, url)
                    assertEquals(model.gameId, gameId)
                    assertEquals(model.width, width)
                    assertEquals(model.height, height)
                }
            }
        }
    }


}