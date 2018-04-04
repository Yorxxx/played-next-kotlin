package com.piticlistudio.playednext.data.entity.igdb

import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class IGDBImageTest {

    @DisplayName("Given a IGDBImage instance")
    @Nested
    inner class IGDBImageInstance {

        private lateinit var image: IGDBImage
        private val url = "//images.igdb.com/igdb/image/upload/t_thumb/kh5qruqeea8zeiq9phiw.jpg"

        @BeforeEach
        internal fun setUp() {
            image = IGDBImage(url, randomString(), randomInt(), randomInt())
        }

        @DisplayName("it should return correct mediumUrl")
        @Test
        fun mediumUrl() {
            assertEquals("https://images.igdb.com/igdb/image/upload/t_screenshot_med/kh5qruqeea8zeiq9phiw.jpg", image.mediumSizeUrl)
        }
    }

    @DisplayName("Given a IGDBImage instance with http preffix")
    @Nested
    inner class IGDBImageInstanceWithHTTP {

        private lateinit var image: IGDBImage
        private val url = "http://images.igdb.com/igdb/image/upload/t_thumb/kh5qruqeea8zeiq9phiw.jpg"

        @BeforeEach
        internal fun setUp() {
            image = IGDBImage(url, randomString(), randomInt(), randomInt())
        }

        @DisplayName("it should return correct mediumUrl")
        @Test
        fun mediumUrl() {
            assertEquals("https://images.igdb.com/igdb/image/upload/t_screenshot_med/kh5qruqeea8zeiq9phiw.jpg", image.mediumSizeUrl)
        }
    }

    @DisplayName("Given a IGDBImage instance with https preffix")
    @Nested
    inner class IGDBImageInstanceWithHTTPS {

        private lateinit var image: IGDBImage
        private val url = "https://images.igdb.com/igdb/image/upload/t_thumb/kh5qruqeea8zeiq9phiw.jpg"

        @BeforeEach
        internal fun setUp() {
            image = IGDBImage(url, randomString(), randomInt(), randomInt())
        }

        @DisplayName("it should return correct mediumUrl")
        @Test
        fun mediumUrl() {
            assertEquals("https://images.igdb.com/igdb/image/upload/t_screenshot_med/kh5qruqeea8zeiq9phiw.jpg", image.mediumSizeUrl)
        }
    }
}