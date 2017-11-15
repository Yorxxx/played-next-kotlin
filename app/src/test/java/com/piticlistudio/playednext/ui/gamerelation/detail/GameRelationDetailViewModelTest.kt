package com.piticlistudio.playednext.ui.gamerelation.detail

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.interactor.relation.LoadGameRelationUseCase
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GameRelationDetailViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @JvmField
    @Rule
    val taskRule = InstantTaskExecutorRule()

    private lateinit var viewModel: GameRelationDetailViewModel

    @Mock private lateinit var usecase: LoadGameRelationUseCase
    @Mock private lateinit var dataObserver: Observer<GameRelation?>
    @Mock private lateinit var loadingObserver: Observer<Boolean>
    @Mock private lateinit var errorObserver: Observer<Throwable?>
    private val relation = makeGameRelation()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = GameRelationDetailViewModel(usecase)
        viewModel.getLoading().observeForever(loadingObserver)
        viewModel.getData().observeForever(dataObserver)
        viewModel.getError().observeForever(errorObserver)
        val flow = Flowable.create<GameRelation>({ it.onNext(relation) }, BackpressureStrategy.MISSING)
        whenever(usecase.execute(any())).thenReturn(flow)
    }

    @Test
    fun fetchDataTriggersLoadingState() {
        // Act
        viewModel.loadRelation()

        verify(loadingObserver).onChanged(true)
    }

    @Test
    fun fetchDataTriggersUseCase() {

        viewModel.loadRelation()

        verify(usecase).execute(Pair(10, 11))
    }

    @Test
    fun fetchDataSuccessTriggersSuccessState() {
        viewModel.loadRelation()

        verify(dataObserver).onChanged(relation)
        verify(loadingObserver).onChanged(false)
        verify(errorObserver).onChanged(null)
    }

    @Test
    fun fetchDataErrorTriggersState() {
        val error = Throwable("foo")
        whenever(usecase.execute(any())).thenReturn(Flowable.error(error))

        viewModel.loadRelation()

        verify(dataObserver).onChanged(null)
        verify(loadingObserver).onChanged(false)
        verify(errorObserver).onChanged(error)
    }
}