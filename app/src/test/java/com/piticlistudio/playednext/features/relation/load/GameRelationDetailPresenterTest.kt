package com.piticlistudio.playednext.features.relation.load

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.domain.interactor.relation.LoadGameRelationUseCase
import com.piticlistudio.playednext.domain.interactor.relation.SaveGameRelationUseCase
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class GameRelationDetailPresenterTest {

    private lateinit var presenter: GameRelationDetailPresenter
    @Mock private lateinit var loadUseCase: LoadGameRelationUseCase
    @Mock private lateinit var saveUseCase: SaveGameRelationUseCase
    @Mock private lateinit var view: GameRelationDetailContract.View
    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()
    private val gameid = 10
    private val platformId = 20

    @Before
    internal fun setUp() {
        MockitoAnnotations.initMocks(this)
        val flowable = Flowable.create<GameRelation>({
            it.onNext(makeGameRelation())
            it.onNext(makeGameRelation())
        }, BackpressureStrategy.MISSING)
        whenever(loadUseCase.execute(any())).thenReturn(flowable)
        presenter = GameRelationDetailPresenter(loadUseCase, saveUseCase)
        presenter.attachView(view)
    }

    @org.junit.Test
    @DisplayName("Should show loading when load is called")
    fun shouldShowLoading() {
        presenter.load(gameid, platformId)
        verify(view).showLoading()
    }

    @org.junit.Test
    @DisplayName("Should hide error when load is called")
    fun shouldHideError() {
        presenter.load(gameid, platformId)
        verify(view).hideError()
    }

    @org.junit.Test
    @DisplayName("Should hide data when load is called")
    fun shouldHideData() {
        presenter.load(gameid, platformId)
        verify(view).hideData()
    }

    @org.junit.Test
    @DisplayName("Should call loadUseCase when load is called")
    fun loadUseCaseIsCalled() {
        presenter.load(gameid, platformId)
        verify(loadUseCase).execute(Pair(gameid, platformId))
    }

    @org.junit.Test
    @DisplayName("Should show data when load succeeds")
    fun showsData() {
        val flowable = Flowable.create<GameRelation>({
            it.onNext(makeGameRelation())
            it.onNext(makeGameRelation())
        }, BackpressureStrategy.MISSING)
        whenever(loadUseCase.execute(any())).thenReturn(flowable)
        presenter.load(gameid, platformId)
        verify(view, times(2)).showData(any())
        verify(view, times(2)).hideLoading()
    }

    @org.junit.Test
    @DisplayName("Should show error when load fails")
    fun showsError() {
        val error = Throwable("foo")
        whenever(loadUseCase.execute(any())).thenReturn(Flowable.error(error))
        presenter.load(gameid, platformId)
        verify(view, never()).showData(any())
        verify(view).showError(error)
        verify(view).hideLoading()
    }

}