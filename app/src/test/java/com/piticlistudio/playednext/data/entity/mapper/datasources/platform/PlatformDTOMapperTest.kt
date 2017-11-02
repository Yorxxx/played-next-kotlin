package com.piticlistudio.playednext.data.entity.mapper.datasources.platform

import com.piticlistudio.playednext.data.entity.net.PlatformDTO
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatformDTOList
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

internal class PlatformDTOMapperTest {

    @Nested
    @DisplayName("Given a PlatformDTOMapper instance")
    inner class instance {

        private lateinit var mapper: PlatformDTOMapper

        @BeforeEach
        internal fun setUp() {
            mapper = PlatformDTOMapper()
        }

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class mapFromModelCalled {

            private val model = makePlatformDTOList()
            private var result: List<Platform> = listOf()

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromModel(model)
            }

            @Test
            @DisplayName("Then should map model")
            fun isMapped() {
                assertNotNull(result)
                assertEquals(model.size, result.size)
                model.forEachIndexed { index, platformDao ->
                    assertEquals(platformDao.created_at, result.get(index).createdAt)
                    assertEquals(platformDao.updated_at, result.get(index).updatedAt)
                    assertEquals(platformDao.id, result.get(index).id)
                    assertEquals(platformDao.name, result.get(index).name)
                    assertEquals(platformDao.slug, result.get(index).slug)
                    assertEquals(platformDao.url, result.get(index).url)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class mapFromEntity {

            private val sources = listOf<Platform>()
            private var result: List<PlatformDTO>? = null

            @Test
            @DisplayName("Then throws error")
            fun errorThrown() {
                try {
                    result = mapper.mapFromEntity(sources)
                    Assertions.fail<String>("Should have thrown")
                } catch (e: Throwable) {
                    kotlin.test.assertNull(result)
                }
            }
        }
    }
}