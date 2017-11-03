package com.piticlistudio.playednext.data.repository.datasource.dao.relation

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.datasources.relation.RelationDaoMapper
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.test.factory.DataFactory
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelationDao
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class RelationDaoRepositoryImplTest {

    @Nested
    @DisplayName("Given a RelationDaoRepositoryImpl instance")
    inner class instance {

        private lateinit var repository: RelationDaoRepositoryImpl
        @Mock
        private lateinit var dao: RelationDaoService
        @Mock
        private lateinit var mapper: RelationDaoMapper

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = RelationDaoRepositoryImpl(dao, mapper)
        }

        @Nested
        @DisplayName("When we call save")
        inner class saveCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makeGameRelation()
            private val result = makeGameRelationDao()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromEntity(source)).thenReturn(result)
                whenever(dao.insert(result)).thenReturn(DataFactory.randomLong())
                observer = repository.save(source).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).insert(result)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertNoValues()
                }
            }
        }

        @Nested
        @DisplayName("When we call loadForGameAndPlatform")
        inner class loadForGameAndPlatformCalled {

            private var observer: TestObserver<GameRelation>? = null
            private val source = makeGameRelationDao()
            private val result = makeGameRelation()
            private val gameId = 10
            private val platformId = 5

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromModel(source)).thenReturn(result)
                whenever(dao.findForGameAndPlatform(gameId, platformId)).thenReturn(Single.just(source))
                observer = repository.loadForGameAndPlatform(gameId, platformId).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestDao() {
                verify(dao).findForGameAndPlatform(gameId, platformId)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertValue(result)
                }
            }
        }
    }
}