package com.piticlistudio.playednext.features.game.load

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.game.SaveGameUseCase
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class GameLoadPresenterTest {

    @Mock
    private lateinit var useCase: LoadGameUseCase
    @Mock
    private lateinit var saveUseCase: SaveGameUseCase
    @Mock
    private lateinit var view: GameLoadContract.View
    private lateinit var presenter: GameLoadPresenter
    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    private val result1 = makeGame()

    @Before
    internal fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = GameLoadPresenter(useCase, saveUseCase)
        presenter.attachView(view)
        whenever(useCase.execute(anyInt())).thenReturn(Single.just(result1))
        whenever(saveUseCase.execute(result1)).thenReturn(Completable.complete())
    }

    @org.junit.Test
    fun showLoadingIsCalled() {
        presenter.load(result1.id)
        verify(view).showLoading()
    }

    @org.junit.Test
    fun hideErrorIsCalled() {
        presenter.load(result1.id)
        verify(view).hideError()
    }

    @org.junit.Test
    fun useCaseIsCalled() {
        presenter.load(result1.id)
        verify(useCase).execute(result1.id)
    }

    @org.junit.Test
    fun whenLoadUseCaseReturnsResult() {
        whenever(useCase.execute(result1.id)).thenReturn(Single.just(result1))
        whenever(saveUseCase.execute(result1)).thenReturn(Completable.complete())
        presenter.load(result1.id)

        verify(view).showData(result1)
        verify(view, never()).showError(any())
        verify(view).hideLoading()
        verify(view).hideError()
        verify(saveUseCase).execute(result1)
    }

    @org.junit.Test
    fun whenSaveUseCaseReturnsError() {
        val error = Throwable("bla")
        whenever(useCase.execute(result1.id)).thenReturn(Single.just(result1))
        whenever(saveUseCase.execute(result1)).thenReturn(Completable.error(error))
        presenter.load(result1.id)

        verify(view).showData(any())
        verify(view, never()).showError(error)
        verify(view).hideLoading()
    }

    @org.junit.Test
    fun whenUseCaseReturnsError() {
        val error = Throwable("bla")
        whenever(useCase.execute(anyInt())).thenReturn(Single.error(error))
        presenter.load(10)

        verify(view, never()).showData(any())
        verify(view).showError(error)
        verify(view).hideLoading()
        verify(view).hideData()
        verifyZeroInteractions(saveUseCase)
    }
}