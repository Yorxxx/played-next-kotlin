package com.piticlistudio.playednext.data.repository.datasource.dao

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.GameDomainModel
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDaoMapper
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameCache
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameEntity
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class GameLocalImplTest {

    @Nested
    @DisplayName("Given GameLocalImpl instance")
    inner class localImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock
        private lateinit var daoService: GameDaoService
        @Mock
        private lateinit var mapper: GameDaoMapper

        private lateinit var repository: GameLocalImpl

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = GameLocalImpl(daoService, mapper)
        }

        @Nested
        @DisplayName("When load is called")
        inner class loadIsCalled {

            private var observer: TestObserver<GameDomainModel>? = null
            private val model = makeGameCache()
            private val entity = makeGameEntity()

            @BeforeEach
            internal fun setUp() {
                whenever(daoService.findGameById(10))
                        .thenReturn(Single.just(model))
                whenever(mapper.mapFromModel(model)).thenReturn(entity)
                observer = repository.load(10).test()
            }

            @Test
            @DisplayName("Then should request DAO")
            fun daoIsCalled() {
                verify(daoService).findGameById(10)
            }

            @Test
            @DisplayName("Then should map daoService result")
            fun mapIsCalled() {
                verify(mapper).mapFromModel(model)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertComplete()
                    assertValueCount(1)
                    assertNoErrors()
                    assertValue(entity)
                }
            }
        }

        @Nested
        @DisplayName("When save is called")
        inner class saveIsCalled {

            private val entity = makeGameEntity()
            private val model = makeGameCache()
            private var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromEntity(entity)).thenReturn(model)
                observer = repository.save(entity).test()
            }

            @Test
            @DisplayName("Then maps entity into Dao model")
            fun mapsEntity() {
                verify(mapper).mapFromEntity(entity)
            }

            @Test
            @DisplayName("Then inserts into DAO")
            fun isInserted() {
                verify(daoService).insertGame(model)
            }

            @Test
            @DisplayName("Then emits completion")
            fun emitsComplete() {
                assertNotNull(observer)
                observer?.apply {
                    assertComplete()
                    assertNoValues()
                    assertNoErrors()
                }
            }
        }

        @Nested
        @DisplayName("When search is called")
        inner class SearchCalled {

            private val model1 = makeGameCache()
            private val model2 = makeGameCache()
            private var observer: TestObserver<List<GameDomainModel>>? = null
            private val entity1 = makeGameEntity()
            private val entity2 = makeGameEntity()

            @BeforeEach
            internal fun setUp() {
                whenever(daoService.findByName("foo")).thenReturn(Flowable.just(listOf(model1, model2)))
                whenever(mapper.mapFromModel(model1)).thenReturn(entity1)
                whenever(mapper.mapFromModel(model2)).thenReturn(entity2)
                observer = repository.search("foo").test()
            }

            @Test
            @DisplayName("Then searches on DAO")
            fun daoIsCalled() {
                verify(daoService).findByName("foo")
            }

            @Test
            @DisplayName("Then maps result into GameEntities")
            fun isMapped() {
                verify(mapper).mapFromModel(model1)
                verify(mapper).mapFromModel(model2)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                with(observer) {
                    this!!.assertNoErrors()
                    assertValueCount(1)
                    assertComplete()
                    assertValue(listOf(entity1, entity2))
                }
            }
        }
    }
}