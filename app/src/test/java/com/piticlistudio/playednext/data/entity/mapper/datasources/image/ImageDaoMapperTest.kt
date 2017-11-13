package com.piticlistudio.playednext.data.entity.mapper.datasources.image

import com.piticlistudio.playednext.data.entity.dao.ScreenshotDao
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGameImage
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGameImageDao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertNull

internal class ImageDaoMapperTest {

    @Nested
    @DisplayName("Given a ImageDaoMapper instance")
    inner class ImageDaoMapperInstance {

        private lateinit var mapper: ImageDaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = ImageDaoMapper()
        }

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class MapFromModelCalled {

            private val model = makeGameImageDao()
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
                    assertEquals(model.id, id)
                    assertEquals(model.width, width)
                    assertEquals(model.url, url)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class MapFromEntityCalled {

            private val entity = makeGameImage()
            private var result: ScreenshotDao? = null

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
                    assertEquals(entity.id, id)
                    assertEquals(entity.url, url)
                    assertEquals(entity.width, width)
                    assertNull(gameId)
                }
            }
        }
    }
}