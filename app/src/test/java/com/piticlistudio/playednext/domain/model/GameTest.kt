package com.piticlistudio.playednext.domain.model

import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompany
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenre
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GameTest {

    @Nested
    @DisplayName("Given a Game instance")
    inner class GameInstance {

        lateinit var game: Game
        val companies = listOf<Company>(makeCompany("Nintendo"), makeCompany("Microsoft"), makeCompany("Sega"))
        val companies2 = listOf<Company>(makeCompany("Sony"), makeCompany("Electronic Arts"), makeCompany("Ubisoft"))
        val genres = listOf<Genre>(makeGenre("Adventure"), makeGenre("Action"), makeGenre("RPG"))

        @BeforeEach
        internal fun setUp() {
            game = makeGame(developers = companies, publishers = companies2, genres = genres);
        }

        @Test
        @DisplayName("Then should return developers name")
        fun developersName() {
            assertNotNull(game.developersName)
            assertEquals("Nintendo, Microsoft, Sega", game.developersName)
        }

        @Test
        @DisplayName("Then should return publishers name")
        fun publishersName() {
            assertNotNull(game.publishersName)
            assertEquals("Sony, Electronic Arts, Ubisoft", game.publishersName)
        }

        @Test
        @DisplayName("Then should return genres name")
        fun genresName() {
            assertNotNull(game.genresName)
            assertEquals("Adventure, Action, RPG", game.genresName)
        }
    }
}