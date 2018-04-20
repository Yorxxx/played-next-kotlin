package com.piticlistudio.playednext.data.entity.mapper.datasources.platform

import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.factory.PlatformFactory.Factory.makeIGDBPlatform
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class IGDBPlatformMapperTest {

    @Nested
    @DisplayName("Given a IGDBPlatformMapper instance")
    inner class Instance {

        private lateinit var mapper: IGDBPlatformMapper

        @BeforeEach
        internal fun setUp() {
            mapper = IGDBPlatformMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDataLayer")
        inner class MapFromModelCalled {

            private val model = makeIGDBPlatform()
            private var result: Platform? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDataLayer(model)
            }

            @Test
            @DisplayName("Then should map model")
            fun isMapped() {
                assertNotNull(result)
                result?.let {
                    assertEquals(model.id, it.id)
                    assertEquals(model.created_at, it.createdAt)
                    assertEquals(model.updated_at, it.updatedAt)
                    assertEquals(model.name, it.name)
                    assertEquals(model.url, it.url)
                }
            }
        }
    }
}