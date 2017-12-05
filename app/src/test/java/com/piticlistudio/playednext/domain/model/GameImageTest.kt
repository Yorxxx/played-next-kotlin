package com.piticlistudio.playednext.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GameImageTest {

    @DisplayName("Given a GameImage instance")
    @Nested
    inner class GameImageInstance {

        private lateinit var image: GameImage
        private val url = "//images.igdb.com/igdb/image/upload/t_thumb/kh5qruqeea8zeiq9phiw.jpg"

        @BeforeEach
        internal fun setUp() {
            image = GameImage("id", url, 100, 200)
        }

        @DisplayName("it should return correct mediumUrl")
        @Test
        fun mediumUrl() {
            assertEquals("https://images.igdb.com/igdb/image/upload/t_screenshot_med/kh5qruqeea8zeiq9phiw.jpg", image.mediumSizeUrl)
        }
    }

    @DisplayName("Given a GameImage instance with http preffix")
    @Nested
    inner class GameImageInstanceWithHTTP {

        private lateinit var image: GameImage
        private val url = "http://images.igdb.com/igdb/image/upload/t_thumb/kh5qruqeea8zeiq9phiw.jpg"

        @BeforeEach
        internal fun setUp() {
            image = GameImage("id", url, 100, 200)
        }

        @DisplayName("it should return correct mediumUrl")
        @Test
        fun mediumUrl() {
            assertEquals("https://images.igdb.com/igdb/image/upload/t_screenshot_med/kh5qruqeea8zeiq9phiw.jpg", image.mediumSizeUrl)
        }
    }

    @DisplayName("Given a GameImage instance with https preffix")
    @Nested
    inner class GameImageInstanceWithHTTPS {

        private lateinit var image: GameImage
        private val url = "https://images.igdb.com/igdb/image/upload/t_thumb/kh5qruqeea8zeiq9phiw.jpg"

        @BeforeEach
        internal fun setUp() {
            image = GameImage("id", url, 100, 200)
        }

        @DisplayName("it should return correct mediumUrl")
        @Test
        fun mediumUrl() {
            assertEquals("https://images.igdb.com/igdb/image/upload/t_screenshot_med/kh5qruqeea8zeiq9phiw.jpg", image.mediumSizeUrl)
        }
    }
}