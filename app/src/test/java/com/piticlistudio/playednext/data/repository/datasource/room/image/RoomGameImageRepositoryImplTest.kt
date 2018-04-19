package com.piticlistudio.playednext.data.repository.datasource.room.image

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.mapper.datasources.image.RoomGameImageMapper
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.factory.GameImageFactory.Factory.makeGameImage
import com.piticlistudio.playednext.factory.GameImageFactory.Factory.makeRoomGameImage
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.*

internal class RoomGameImageRepositoryImplTest {

    @Nested
    @DisplayName("Given a RoomGameImageRepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: RoomGameImageRepositoryImpl
        private val dao: RoomGameImagesService = mock()
        private val mapper: RoomGameImageMapper = mock()

        @BeforeEach
        internal fun setUp() {
            reset(dao, mapper)
            repository = RoomGameImageRepositoryImpl(dao, mapper)
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class LoadCalled {

            private var observer: TestSubscriber<List<GameImage>>? = null
            private val source = makeRoomGameImage()
            private val result = makeGameImage()

            @BeforeEach
            internal fun setUp() {
                whenever(dao.findForGame(10)).thenReturn(Flowable.just(listOf(source)))
                whenever(mapper.mapFromDataLayer(source)).thenReturn(result)
                observer = repository.loadForGame(10).test()
            }

            @Test
            @DisplayName("Then should request dao service")
            fun shouldRequestRepository() {
                verify(dao).findForGame(10)
            }

            @Test
            @DisplayName("Then should map response")
            fun shouldMap() {
                verify(mapper).mapFromDataLayer(source)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                Assertions.assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertValueCount(1)
                    assertValue {
                        it.size == 1 && it.contains(result)
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call saveForGame")
        inner class SaveForGameCalled {

            private var observer: TestObserver<Void>? = null
            private val source = makeGameImage()
            private val result = makeRoomGameImage()

            @BeforeEach
            internal fun setUp() {
                whenever(mapper.mapIntoDataLayerModel(source)).thenReturn(result)
                whenever(dao.insert(result)).thenReturn(10)
                observer = repository.saveForGame(source).test()
            }

            @Test
            @DisplayName("Then should save gameimage")
            fun shouldSaveCompany() {
                verify(dao).insert(result)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                Assertions.assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertNoValues()
                }
            }

            @Nested
            @DisplayName("And Room fails to save")
            inner class RoomFail {

                @BeforeEach
                internal fun setUp() {
                    reset(dao, mapper)
                    whenever(dao.insert(result)).thenReturn(-1)

                    observer = repository.saveForGame(source).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun emitsError() {
                    Assertions.assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertError(GameImageSaveException::class.java)
                        assertNotComplete()
                    }
                }
            }
        }
    }
}