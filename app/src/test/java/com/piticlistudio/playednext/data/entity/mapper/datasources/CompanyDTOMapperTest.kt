package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.net.CompanyDTO
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyDTOList
import org.junit.jupiter.api.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class CompanyDTOMapperTest {

    @Nested
    @DisplayName("Given CompanyDTOMapper instance")
    inner class mapperInstance {

        private lateinit var mapper: CompanyDTOMapper

        @BeforeEach
        internal fun setUp() {
            mapper = CompanyDTOMapper()
        }

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class mapFromModelCalled {

            val sources = makeCompanyDTOList()
            var result: List<Company>? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromModel(sources)
            }

            @Test
            @DisplayName("Then should map into company list")
            fun mapped() {
                assertNotNull(result)
                with(result!!) {
                    assertEquals(sources.size, size)
                    for ((index,value) in this.withIndex()) {
                        assertEquals(sources.get(index).id, value.id)
                        assertEquals(sources.get(index).name, value.name)
                        assertEquals(sources.get(index).created_at, value.createdAt)
                        assertEquals(sources.get(index).updated_at, value.updatedAt)
                        assertEquals(sources.get(index).slug, value.slug)
                        assertEquals(sources.get(index).url, value.url)
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class mapFromEntity {

            private val sources = listOf<Company>()
            private var result: List<CompanyDTO>? = null

            @Test
            @DisplayName("Then throws error")
            fun errorThrown() {
                try {
                    result = mapper.mapFromEntity(sources)
                    Assertions.fail<String>("Should have thrown")
                } catch (e: Throwable) {
                    assertNull(result)
                }
            }
        }
    }
}