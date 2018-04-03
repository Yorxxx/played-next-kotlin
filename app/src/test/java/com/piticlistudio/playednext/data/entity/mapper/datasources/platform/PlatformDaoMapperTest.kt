package com.piticlistudio.playednext.data.entity.mapper.datasources.platform

import com.piticlistudio.playednext.data.entity.room.PlatformDao
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatformDao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlatformDaoMapperTest {

    @Nested
    @DisplayName("Given a PlatformDaoMapper instance")
    inner class Instance {

        private lateinit var mapper: PlatformDaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = PlatformDaoMapper()
        }

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class MapFromModelCalled {

            private val model = randomListOf{ makePlatformDao() }
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
                    assertEquals(platformDao.created_at, result[index].createdAt)
                    assertEquals(platformDao.updated_at, result[index].updatedAt)
                    assertEquals(platformDao.id, result[index].id)
                    assertEquals(platformDao.name, result[index].name)
                    assertEquals(platformDao.slug, result[index].slug)
                    assertEquals(platformDao.url, result[index].url)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class MapFromEntityCalled {

            private val model = randomListOf { makePlatform() }
            private var result: List<PlatformDao>? = null

            @BeforeEach
            internal fun setup() {
                result = mapper.mapFromEntity(model)
            }

            @Test
            @DisplayName("Then should map entity")
            fun isMapped() {
                assertNotNull(result)
                assertEquals(model.size, result?.size)
                model.forEachIndexed { index, platform ->
                    assertEquals(platform.createdAt, result?.get(index)?.created_at)
                    assertEquals(platform.updatedAt, result?.get(index)?.updated_at)
                    assertEquals(platform.id, result?.get(index)?.id)
                    assertEquals(platform.name, result?.get(index)?.name)
                    assertEquals(platform.slug, result?.get(index)?.slug)
                    assertEquals(platform.url, result?.get(index)?.url)
                }
            }
        }
    }
}