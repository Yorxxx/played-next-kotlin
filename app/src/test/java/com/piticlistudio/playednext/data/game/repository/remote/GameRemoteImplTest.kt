package com.piticlistudio.playednext.data.game.repository.remote

import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import kotlin.test.assertNotNull

class GameRemoteImplTest {

    /*@Nested
    @DisplayName("Given a GameRemoteImpl instance")
    inner class GameRemoteImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock lateinit var service: GameService
        @Mock lateinit var mapper: IGDBGameMapper

        private var repositoryImpl: GameRemoteImpl? = null

        @BeforeEach
        fun setup() {
            MockitoAnnotations.initMocks(this)
            repositoryImpl = GameRemoteImpl(service, mapper)
        }

        @Nested
        @DisplayName("When we call load")
        inner class Load {

            var result: TestObserver<GameEntity>? = null
            val model = IGDBGameModel(10, "name", "summary", "story", 0, 1, 2f)
            val entity = GameEntity(10, "name", "summary", "storyline", 0, 1, 2f)

            @BeforeEach
            fun setup() {
                val response = listOf(model)

                `when`(service.load(10, "*"))
                        .thenReturn(Single.just(response))
                `when`(mapper.mapFromModel(model)).thenReturn(entity)
                result = repositoryImpl?.load(10)?.test()
            }

            @Test
            @DisplayName("Then should request service")
            fun serviceIsCalled() {
                verify(service).load(10, "*")
            }

            @Test
            @DisplayName("Then should map service response")
            fun mapIsCalled() {
                verify(mapper).mapFromModel(model)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(result)
                with(result) {
                    this?.assertValueCount(1)
                    this?.assertComplete()
                    this?.assertNoErrors()
                    this?.assertValue(entity)
                }
            }

            @Nested
            @DisplayName("And returns empty list")
            inner class EmptyResponse {

                @BeforeEach
                fun setup() {
                    `when`(service.load(10, "*"))
                            .thenReturn(Single.just(listOf()))
                    result = repositoryImpl?.load(10)?.test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    with(result) {
                        this?.assertNoValues()
                        this?.assertNotComplete()
                        this?.assertError(Throwable::class.java)
                    }
                }
            }

            @Nested
            @DisplayName("And returns list with multiple items")
            inner class MultipleListResponse {

                @BeforeEach
                fun setup() {
                    val response = listOf(IGDBGameModel(10, "name", "summary", "story", 0, 1, 2f),
                            IGDBGameModel(10, "name2", "summary2", "story2", 0, 1, 2f))
                    `when`(service.load(10, "*"))
                            .thenReturn(Single.just(response))
                    result = repositoryImpl?.load(10)?.test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    with(result) {
                        this?.assertNoValues()
                        this?.assertNotComplete()
                        this?.assertError(Throwable::class.java)
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call search")
        inner class Search {

            val model = IGDBGameModel(10, "name", "summary", "story", 0, 1, 2f)
            val model2 = IGDBGameModel(11, "name2", "summary2", "story2", 1, 2, 3f)
            val response = listOf(model, model2)
            val entity1 = GameEntity(10, "name", "summary", "story", 1, 2, 3f)
            val entity2 = GameEntity(10, "name", "summary", "story", 1, 2, 3f)
            var result: TestObserver<List<GameEntity>>? = null

            @BeforeEach
            fun setup() {
                `when`(service.search(0, "query", "*", 20))
                        .thenReturn(Single.just(response))
                `when`(mapper.mapFromModel(model))
                        .thenReturn(entity1)
                `when`(mapper.mapFromModel(model2))
                        .thenReturn(entity2)
                result = repositoryImpl?.search("query")?.test()
            }

            @Test
            @DisplayName("Then should request service with correct params")
            fun serviceIsCalled() {
                verify(service).search(0, "query", "*", 20)
            }

            @Test
            @DisplayName("Then maps result into data model")
            fun mapIsCalled() {
                verify(mapper).mapFromModel(model)
                verify(mapper).mapFromModel(model2)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutError() {
                assertNotNull(result)
                with(result) {
                    this!!.assertNoErrors()
                    assertComplete()
                    assertValueCount(1)
                    assertValue(listOf(entity1, entity2))
                }
            }
        }

        @Nested
        @DisplayName("When we call save")
        inner class Save {

            val entity1 = GameEntity(10, "name", "summary", "story", 1, 2, 3f)
            var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                observer = repositoryImpl?.save(entity1)?.test()
            }

            @Test
            @DisplayName("Then should emit notAllowedException")
            fun throwsError() {
                assertNotNull(observer)
                with(observer) {
                    this!!.assertNotComplete()
                    assertNoValues()
                    assertError(Throwable::class.java)
                }
            }
        }
    }*/
}