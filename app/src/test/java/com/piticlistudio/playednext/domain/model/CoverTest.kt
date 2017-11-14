package com.piticlistudio.playednext.domain.model

import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CoverTest {

    @Nested
    @DisplayName("Given a Cover instance")
    inner class CoverInstance {

        private lateinit var cover: Cover
        private val url = "//images.igdb.com/igdb/image/upload/t_thumb/kh5qruqeea8zeiq9phiw.jpg"

        @BeforeEach
        internal fun setUp() {
            cover = Cover(url, randomInt(), randomInt())
        }

        @Test
        @DisplayName("Then should return small cover value")
        fun smallCoverValue() {
            assertEquals("http://images.igdb.com/igdb/image/upload/t_cover_small/kh5qruqeea8zeiq9phiw.jpg", cover.smallUrl)
        }

        @Test
        @DisplayName("Then should return big cover value")
        fun bigCoverValue() {
            assertEquals("http://images.igdb.com/igdb/image/upload/t_cover_big/kh5qruqeea8zeiq9phiw.jpg", cover.bigUrl)
        }
    }
}