package com.piticlistudio.playednext.data.entity.mapper.datasources.image

import com.piticlistudio.playednext.data.entity.net.ImageDTO
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.test.factory.GameImageFactory
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeImageDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ImageDTOMapperTest {

    @Nested
    @DisplayName("Given a ImageDTOMapper instance")
    inner class ImageDTOMapperInstance {

        private lateinit var mapper: ImageDTOMapper

        @BeforeEach
        internal fun setUp() {
            mapper = ImageDTOMapper()
        }

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class MapFromModelCalled {

            private val model = makeImageDTO()
            private var result: GameImage? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromModel(model)
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

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class MapFromEntityCalled {

            private val entity = GameImageFactory.makeGameImage()
            private var result: ImageDTO? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromEntity(entity)
            }

            @Test
            @DisplayName("Then should map result")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(entity.height, height)
                    assertEquals(entity.id, cloudinary_id)
                    assertEquals(entity.url, url)
                    assertEquals(entity.width, width)
                }
            }
        }
    }
}