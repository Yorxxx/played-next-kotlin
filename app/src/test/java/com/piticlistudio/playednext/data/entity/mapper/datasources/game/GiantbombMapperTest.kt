package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Test cases for [GiantbombMapper]
 * Created by e-jegi on 3/26/2018.
 */
internal class GiantbombMapperTest {

    private val mapper = GiantbombMapper()
    private lateinit var game: Game

    @BeforeEach
    internal fun setUp() {
        game = makeGame()
    }

    @Test
    fun `should map Giantbomb game into domain game`() {

    }
}