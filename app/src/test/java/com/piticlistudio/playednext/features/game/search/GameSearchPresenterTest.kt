package com.piticlistudio.playednext.features.game.search

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class GameSearchPresenterTest {

    @Mock private lateinit var useCase: SearchGamesUseCase
    @Mock private lateinit var view: GameSearchContract.View

    private lateinit var presenter: GameSearchPresenter
    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    private val result1 = makeGame()
    private val result2 = makeGame()
    private val result3 = makeGame()

    @Before
    internal fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = GameSearchPresenter(useCase)
        presenter.attachView(view)

        val flowable = Flowable.create<List<Game>>({ it.onNext(listOf()) }, BackpressureStrategy.MISSING)
        whenever(useCase.execute(ArgumentMatchers.anyString())).thenReturn(flowable)
    }

    @Test
    fun showLoadingIsCalled() {
        presenter.search("foo")
        verify(view).showLoading()
    }

    @Test
    fun hideErrorIsCalled() {
        presenter.search("foo")
        verify(view).hideError()
    }

    @Test
    fun useCaseIsCalled() {
        presenter.search("foo")
        verify(useCase).execute("foo")
    }

    @Test
    fun whenUseCaseReturnsResult() {
        val flowable = Flowable.create<List<Game>>({ it.onNext(listOf(result1, result2, result3)) }, BackpressureStrategy.MISSING)
        whenever(useCase.execute("foo")).thenReturn(flowable)
        presenter.search("foo")

        verify(view).showData(listOf(result1, result2, result3))
        verify(view, never()).showError(any())
        verify(view).hideLoading()
    }

    @Test
    fun whenUseCaseReturnsError() {
        val error = Throwable("bla")
        whenever(useCase.execute("foo")).thenReturn(Flowable.error(error))
        presenter.search("foo")

        verify(view, never()).showData(any())
        verify(view).showError(error)
        verify(view).hideLoading()
    }
}