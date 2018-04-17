package com.piticlistudio.playednext.data.entity.mapper.datasources.relation

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.mapper.datasources.game.RoomGameMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.RoomPlatformMapper
import com.piticlistudio.playednext.data.entity.room.RoomGameRelationProxy
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeRoomGameProxy
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeRoomGameRelationProxy
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makeRoomPlatform
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RoomRelationMapperTest {

    @Nested
    @DisplayName("Given a RoomRelationMapper instance")
    inner class Instance {

        private lateinit var mapper: RoomRelationMapper
        private val gameMapper: RoomGameMapper = mock()
        private val platformMapper: RoomPlatformMapper = mock()

        @BeforeEach
        internal fun setUp() {
            reset(gameMapper, platformMapper)
            mapper = RoomRelationMapper(gameMapper, platformMapper)

            whenever(gameMapper.mapIntoDataLayerModel(anyOrNull())).thenReturn(makeRoomGameProxy())
            whenever(platformMapper.mapIntoDataLayerModel(anyOrNull())).thenReturn(makeRoomPlatform())
            whenever(gameMapper.mapFromDataLayer(anyOrNull())).thenReturn(makeGame())
            whenever(platformMapper.mapFromDataLayer(anyOrNull())).thenReturn(makePlatform())
        }

        @Nested
        @DisplayName("When mapFromDataLayer is called")
        inner class MapFromDataLayerCalled {

            var model = makeRoomGameRelationProxy()
            var result: GameRelation? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDataLayer(model)
            }

            @Test
            @DisplayName("Then should map")
            fun shouldMap() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.relation.created_at, createdAt)
                    assertEquals(model.relation.updated_at, updatedAt)
                    assertEquals(model.relation.status, status.ordinal)
                    verify(gameMapper).mapFromDataLayer(model.game)
                    assertNotNull(game)
                    verify(platformMapper).mapFromDataLayer(model.platform)
                    assertNotNull(platform)
                }
            }
        }

        @Nested
        @DisplayName("When mapIntoDataLayerModel is called")
        inner class MapIntoDataLayerModelCalled {

            var entity = makeGameRelation()
            var result: RoomGameRelationProxy? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapIntoDataLayerModel(entity)
            }

            @Test
            @DisplayName("Then should map")
            fun shouldMap() {
                assertNotNull(result)
                result?.apply {
                    verify(gameMapper).mapIntoDataLayerModel(entity.game)
                    verify(platformMapper).mapIntoDataLayerModel(entity.platform)
                    assertNotNull(game)
                    assertNotNull(platform)
                    assertNotNull(relation)
                    relation.let {
                        assertEquals(entity.createdAt, it.created_at)
                        assertEquals(entity.updatedAt, it.updated_at)
                        assertEquals(entity.status.ordinal, it.status)
                        assertEquals(entity.game.id, it.gameId)
                        assertEquals(entity.platform.id, it.platformId)
                    }
                }
            }
        }
    }
}