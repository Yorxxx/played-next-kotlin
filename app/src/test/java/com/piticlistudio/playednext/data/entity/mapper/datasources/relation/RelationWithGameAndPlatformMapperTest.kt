package com.piticlistudio.playednext.data.entity.mapper.datasources.relation

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.data.entity.dao.PlatformDao
import com.piticlistudio.playednext.data.entity.dao.RelationWithGameAndPlatform
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameCache
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelationDao
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatformDao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class RelationWithGameAndPlatformMapperTest {

    @DisplayName("Given RelationWithGameAndPlatformMapper instance")
    @Nested
    inner class Instance {

        private lateinit var mapper: RelationWithGameAndPlatformMapper
        @Mock private lateinit var gamemapper: DaoModelMapper<GameDao, Game>
        @Mock private lateinit var platformmapper: DaoModelMapper<PlatformDao, Platform>

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            mapper = RelationWithGameAndPlatformMapper(gamemapper, platformmapper)
        }

        @DisplayName("When we call mapFromDao")
        @Nested
        inner class MapFromDaoCalled {

            private var entity = RelationWithGameAndPlatform().apply {
                data = makeGameRelationDao()
                game = listOf(makeGameCache())
                platform = listOf(makePlatformDao())
            }
            private var result: GameRelation? = null

            @BeforeEach
            internal fun setUp() {
                whenever(gamemapper.mapFromDao(any())).thenReturn(makeGame())
                whenever(platformmapper.mapFromDao(any())).thenReturn(makePlatform())
                result = mapper.mapFromDao(entity)
            }

            @Test
            @DisplayName("Then should request GameMapper to map game")
            fun shouldMapGame() {
                verify(gamemapper).mapFromDao(entity.game!!.get(0))
                verifyNoMoreInteractions(gamemapper)
            }

            @Test
            @DisplayName("Then should request PlatformMapper to map platform")
            fun shouldMapPlatform() {
                verify(platformmapper).mapFromDao(entity.platform!!.get(0))
                verifyNoMoreInteractions(platformmapper)
            }

            @Test
            @DisplayName("Then should return a valid relation")
            fun shouldReturnRelation() {
                assertNotNull(result)
                assertEquals(entity.data!!.created_at, result!!.createdAt)
                assertEquals(entity.data!!.updated_at, result!!.updatedAt)
                assertEquals(entity.data!!.status, result!!.currentStatus.ordinal)
                assertNotNull(result!!.game)
                assertNotNull(result!!.platform)
            }
        }

        @DisplayName("When we call mapIntoDao")
        @Nested
        inner class MapIntoDaoCalled {

            private var entity = makeGameRelation()
            private var result: RelationWithGameAndPlatform? = null

            @BeforeEach
            internal fun setUp() {
                whenever(gamemapper.mapIntoDao(any())).thenReturn(makeGameCache())
                whenever(platformmapper.mapIntoDao(any())).thenReturn(makePlatformDao())
                result = mapper.mapIntoDao(entity)
            }

            @Test
            @DisplayName("Then should request GameMapper to map game")
            fun shouldMapGame() {
                verify(gamemapper).mapIntoDao(entity.game!!)
                verifyNoMoreInteractions(gamemapper)
            }

            @Test
            @DisplayName("Then should request PlatformMapper to map platform")
            fun shouldMapPlatform() {
                verify(platformmapper).mapIntoDao(entity.platform!!)
                verifyNoMoreInteractions(platformmapper)
            }

            @Test
            @DisplayName("Then should return valid DAO model")
            fun shouldMapRelation() {
                assertNotNull(result)
                with(result!!) {
                    assertNotNull(game)
                    assertEquals(1, game!!.size)
                    assertNotNull(platform)
                    assertEquals(1, platform!!.size)
                    assertNotNull(data)
                    assertEquals(data!!.status, entity.currentStatus.ordinal)
                    assertEquals(data!!.updated_at, entity.updatedAt)
                    assertEquals(data!!.created_at, entity.createdAt)
                }
            }
        }
    }

}