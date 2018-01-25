package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.ScreenshotDao
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.test.factory.GameImageFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


internal class ImageMapperTest {

    @Nested
    @DisplayName("Given a ImageMapper.DaoMapper instance")
    inner class ImageDaoMapperInstance {

        private lateinit var mapper: ImageMapper.DaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = ImageMapper.DaoMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDao")
        inner class MapFromDaoCalled {

            private val model = GameImageFactory.makeGameImageDao()
            private var result: GameImage? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDao(model)
            }

            @Test
            @DisplayName("Then should map result")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.height, height)
                    assertEquals(model.id, id)
                    assertEquals(model.width, width)
                    assertEquals(model.url, url)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapIntoDao")
        inner class MapIntoDaoCalled {

            private val entity = GameImageFactory.makeGameImage()
            private var result: ScreenshotDao? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapIntoDao(entity)
            }

            @Test
            @DisplayName("Then should map result")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(entity.height, height)
                    assertEquals(entity.id, id)
                    assertEquals(entity.url, url)
                    assertEquals(entity.width, width)
                    kotlin.test.assertNull(gameId)
                }
            }
        }
    }

    @Nested
    @DisplayName("Given a ImageMapper.DTOMapper instance")
    inner class ImageDTOMapperInstance {

        private lateinit var mapper: ImageMapper.DTOMapper

        @BeforeEach
        internal fun setUp() {
            mapper = ImageMapper.DTOMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDTO")
        inner class MapFromDTOCalled {

            private val model = GameImageFactory.makeImageDTO()
            private var result: GameImage? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDTO(model)
            }

            @Test
            @DisplayName("Then should map result")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.height, height)
                    assertEquals(model.cloudinary_id, id)
                    assertEquals(model.width, width)
                    assertEquals(model.url, url)
                }
            }
        }
    }
}