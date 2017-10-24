package com.piticlistudio.playednext.data.repository.datasource.dao

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDaoMapper
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameCache
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

            private var observer: TestObserver<Game>? = null
            private val model = makeGameCache()
            private val entity = makeGame()

            @BeforeEach
            internal fun setUp() {
                whenever(daoService.findGameById(10))
                        .thenReturn(Single.just(model))
                whenever(mapper.mapFromEntity(model)).thenReturn(entity)
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
                verify(mapper).mapFromEntity(model)
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

            private val source = makeGame()
            private val data = makeGameCache()
            private var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromModel(source)).thenReturn(data)
                observer = repository.save(source).test()
            }

            @Test
            @DisplayName("Then maps entity into Dao model")
            fun mapsEntity() {
                verify(mapper).mapFromModel(source)
            }

            @Test
            @DisplayName("Then inserts into DAO")
            fun isInserted() {
                verify(daoService).insertGame(data)
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

            private val data1 = makeGameCache()
            private val data2 = makeGameCache()
            private var observer: TestObserver<List<Game>>? = null
            private val entity1 = makeGame()
            private val entity2 = makeGame()

            @BeforeEach
            internal fun setUp() {
                whenever(daoService.findByName("foo")).thenReturn(Flowable.just(listOf(data1, data2)))
                whenever(mapper.mapFromEntity(data1)).thenReturn(entity1)
                whenever(mapper.mapFromEntity(data2)).thenReturn(entity2)
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
                verify(mapper).mapFromEntity(data1)
                verify(mapper).mapFromEntity(data2)
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