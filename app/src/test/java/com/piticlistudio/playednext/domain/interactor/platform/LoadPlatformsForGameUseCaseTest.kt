package com.piticlistudio.playednext.domain.interactor.platform

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.domain.repository.PlatformRepository
import com.piticlistudio.playednext.test.factory.DataFactory
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.PlatformFactory
import com.piticlistudio.playednext.ui.PlatformUIUtils
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class LoadPlatformsForGameUseCaseTest {

    @Nested
    @DisplayName("Given a LoadGameUseCase instance")
    inner class LoadPlatformsForGameUseCaseInstance {

        @Mock lateinit var repository: PlatformRepository
        @Mock lateinit var uiutils: PlatformUIUtils

        private val game = makeGame()
        private lateinit var useCase: LoadPlatformsForGameUseCase

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            useCase = LoadPlatformsForGameUseCase(repository, uiutils)
        }

        @Nested
        @DisplayName("When we call execute")
        inner class ExecuteIsCalled {

            private var testObserver: TestObserver<List<Platform>>? = null
            val platforms = DataFactory.randomListOf(5) { PlatformFactory.makePlatform() }

            @BeforeEach
            internal fun setUp() {
                whenever(repository.loadForGame(anyInt())).thenReturn(Single.just(platforms))
            }

            @Nested
            @DisplayName("And does have platforms assigned")
            inner class HasPlatforms {

                @BeforeEach
                internal fun setUp() {
                    testObserver = useCase.execute(game).test()
                }

                @Test
                @DisplayName("Then should not request repository")
                fun repositoryIsNotRequested() {
                    verifyZeroInteractions(repository)
                }

                @Test
                @DisplayName("Then should request display values")
                fun requestDisplayValues() {
                    game.platforms?.forEach {
                        verify(uiutils).getColor(it.name)
                        verify(uiutils).getAcronym(it.name)
                    }
                }
            }

            @Nested
            @DisplayName("And does not have platforms assigned")
            inner class HasNotPlatforms {

                @BeforeEach
                internal fun setUp() {
                    game.platforms = listOf()
                    testObserver = useCase.execute(game).test()
                }

                @Test
                @DisplayName("Then should request repository")
                fun repositoryIsNotRequested() {
                    verify(repository).loadForGame(game.id)
                }

                @Test
                @DisplayName("Then should request display values")
                fun requestDisplayValues() {
                    game.platforms?.forEach {
                        verify(uiutils.getColor(it.name))
                        verify(uiutils.getAcronym(it.name))
                    }
                }
            }

            @Nested
            @DisplayName("And platforms is null")
            inner class PlatformsIsNull {

                @BeforeEach
                internal fun setUp() {
                    game.platforms = null
                    testObserver = useCase.execute(game).test()
                }

                @Test
                @DisplayName("Then should request repository")
                fun repositoryIsNotRequested() {
                    verify(repository).loadForGame(game.id)
                }

                @Test
                @DisplayName("Then should request display values")
                fun requestDisplayValues() {
                    game.platforms?.forEach {
                        verify(uiutils.getColor(it.name))
                        verify(uiutils.getAcronym(it.name))
                    }
                }
            }
        }
    }
}