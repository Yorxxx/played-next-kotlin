package com.piticlistudio.playednext.data.repository

import android.arch.persistence.room.EmptyResultSetException
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.GameEntityToDomainMapper
import com.piticlistudio.playednext.data.repository.datasource.dao.GameLocalImpl
import com.piticlistudio.playednext.data.repository.datasource.net.GameRemoteImpl
import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameEntity
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.Completable
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

internal class GameRepositoryImplTest {

    @Nested
    @DisplayName("Given a GameRepository instance")
    inner class GameRepositoryImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock
        private lateinit var remoteImpl: GameRemoteImpl
        @Mock
        private lateinit var localImpl: GameLocalImpl
        @Mock
        private lateinit var mapper: GameEntityToDomainMapper

        private var repository: GameRepositoryImpl? = null

        @BeforeEach
        fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = GameRepositoryImpl(remoteImpl, localImpl, mapper)
        }

        @Nested
        @DisplayName("When we call load")
        inner class Load {
            val response = makeGameEntity()
            val entity = makeGame()
            var result: TestObserver<Game>? = null

            @BeforeEach
            fun setup() {
                whenever(localImpl.load(10)).thenReturn(Single.just(response))
                whenever(mapper.mapFromModel(response)).thenReturn(entity)
                result = repository?.load(10)?.test()
            }

            @Test
            @DisplayName("Then should request local repository")
            fun localIsCalled() {
                verify(localImpl).load(10)
            }

            @Test
            @DisplayName("Then should not request remote repository")
            fun remoteIsNotCalled() {
                verifyZeroInteractions(remoteImpl)
            }

            @Test
            @DisplayName("Then should map emission into domain model")
            fun isMapped() {
                verify(mapper).mapFromModel(response)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(result)
                result?.apply {
                    assertNoErrors()
                    assertValueCount(1)
                    assertComplete()
                    assertValue(entity)
                }
            }

            @Nested
            @DisplayName("And there is no result in local repository")
            inner class withoutLocalResult {

                @BeforeEach
                internal fun setUp() {
                    whenever(localImpl.load(10)).thenReturn(Single.error(EmptyResultSetException("no results")))
                    whenever(remoteImpl.load(10)).thenReturn(Single.just(response))
                    result = repository?.load(10)?.test()
                }

                @Test
                @DisplayName("Then should request remote repository")
                fun remoteIsCalled() {
                    verify(remoteImpl).load(10)
                }

                @Test
                @DisplayName("Then should emit without errors")
                fun withoutErrors() {
                    assertNotNull(result)
                    with(result) {
                        this?.assertNoErrors()
                        this?.assertValueCount(1)
                        this?.assertComplete()
                        this?.assertValue(entity)
                    }
                }
            }

        }

        @Nested
        @DisplayName("When we call search")
        inner class Search {

            val model1 = makeGameEntity()
            val model2 = makeGameEntity()
            var result: TestObserver<List<Game>>? = null
            val entity = makeGame()
            val entity2 = makeGame()

            @BeforeEach
            fun setup() {
                whenever(remoteImpl.search("query")).thenReturn(Single.just(listOf(model1, model2)))
                whenever(mapper.mapFromModel(model1)).thenReturn(entity)
                whenever(mapper.mapFromModel(model2)).thenReturn(entity2)
                result = repository?.search("query")?.test()
            }

            @Test
            @DisplayName("Then should request remote implementation")
            fun remoteIsCalled() {
                verify(remoteImpl).search("query")
            }

            @Test
            @DisplayName("Then should map returned results")
            fun mapIsCalled() {
                verify(mapper).mapFromModel(model1)
                verify(mapper).mapFromModel(model2)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun emissionWithoutErrors() {
                assertNotNull(result)
                with(result) {
                    this?.assertNoErrors()
                    this?.assertComplete()
                    this?.assertValueCount(1)
                    this?.assertValue(listOf(entity, entity2))
                }
            }
        }

        @Nested
        @DisplayName("When we call save")
        inner class Save {

            val entity = makeGame()
            val data = makeGameEntity()
            var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapFromEntity(entity)).thenReturn(data)
                whenever(localImpl.save(data)).thenReturn(Completable.complete())
                observer = repository?.save(entity)?.test()
            }

            @Test
            @DisplayName("Then maps domain model into data model")
            fun isSavesLocally() {
                verify(mapper).mapFromEntity(entity)
            }

            @Test
            @DisplayName("Then saves data model")
            fun dataIsSaved() {
                verify(localImpl).save(data)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                with(observer) {
                    this!!.assertNoErrors()
                    assertComplete()
                    assertNoValues()
                }
            }
        }
    }
}