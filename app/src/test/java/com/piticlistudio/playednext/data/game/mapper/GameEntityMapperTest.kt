package com.piticlistudio.playednext.data.game.mapper

class GameEntityMapperTest {

    /*@Nested
    @DisplayName("Given a GameEntityMapper instance")
    inner class GameEntityMapperInstance {

        val mapper = GameEntityToDomainMapper()

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class mapFromModel {

            val model = GameDomainModel(10, "name", "summary", "storyline", 10, 11, 12.0)
            var result: Game? = null

            @BeforeEach
            fun setup() {
                result = mapper.mapFromModel(model)
            }

            @Test
            @DisplayName("Then returns a domain model")
            fun domainModelNotNull() {
                assertNotNull(result)
            }

            @Test
            @DisplayName("Then maps values into domain model")
            fun valuesAreMapped() {
                with(result) {
                    assertEquals(model.id, this?.id)
                    assertEquals(model.name, this?.name)
                    assertEquals(model.summary, this?.summary)
                    assertEquals(model.storyline, this?.storyline)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromDomain")
        inner class mapFromDomain {

            val domain = Game(10, "name", "summary", "storyline")
            var result: GameDomainModel? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromEntity(domain)
            }

            @Test
            @DisplayName("Then returns data model")
            fun dataModel() {
                assertNotNull(result)
                with(result) {
                    assertEquals(domain.id, this!!.id)
                    assertEquals(domain.name, name)
                    assertEquals(domain.summary, summary)
                    assertEquals(domain.storyline, storyline)
                }
            }
        }
    }*/
}