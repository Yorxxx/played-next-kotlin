package com.piticlistudio.playednext.features.relation.load

import com.nhaarman.mockito_kotlin.verify
import com.piticlistudio.playednext.domain.interactor.relation.LoadGameRelationUseCase
import com.piticlistudio.playednext.domain.interactor.relation.SaveGameRelationUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class GameRelationDetailPresenterTest {

    @Nested
    @DisplayName("Given GameRelationDetailPresenter instance")
    inner class instance {

        private lateinit var presenter: GameRelationDetailPresenter
        @Mock private lateinit var loadUseCase: LoadGameRelationUseCase
        @Mock private lateinit var saveUseCase: SaveGameRelationUseCase
        @Mock private lateinit var view: GameRelationDetailContract.View

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            presenter = GameRelationDetailPresenter(loadUseCase, saveUseCase)
            presenter.attachView(view)
        }

        @Nested
        @DisplayName("When we call load")
        inner class loadCalled {

            private val gameid = 10
            private val platformId = 20

            @BeforeEach
            internal fun setUp() {
                presenter.load(gameid, platformId)
            }

            @Test
            @DisplayName("Then should show loading")
            fun shouldShowLoading() {
                verify(view).showLoading()
            }

            @Test
            @DisplayName("Then should hide error")
            fun shouldHideError() {
                verify(view).hideError()
            }

            @Test
            @DisplayName("Then should hide data")
            fun shouldHideData() {
                verify(view).hideData()
            }

            @Test
            @DisplayName("Then should request load usecase")
            fun loadUseCaseIsCalled() {
                verify(loadUseCase).execute(Pair(gameid, platformId))
            }
        }
    }
}