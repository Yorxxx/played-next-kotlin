package com.piticlistudio.playednext.ui.gamerelation.detail

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.relation.LoadRelationsForGameUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
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

    @Mock private lateinit var loadgameusecase: LoadGameUseCase
    @Mock private lateinit var usecase: LoadRelationsForGameUseCase
    @Mock private lateinit var dataObserver: Observer<List<GameRelation>>
    @Mock private lateinit var loadingObserver: Observer<Boolean>
    @Mock private lateinit var gameObserver: Observer<Game?>
    @Mock private lateinit var errorObserver: Observer<Throwable?>
    @Mock private lateinit var imageObserver: Observer<String>
    private val relations = randomListOf{ makeGameRelation() }
    private val game = makeGame()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = GameRelationDetailViewModel(usecase, loadgameusecase)
        viewModel.getLoading().observeForever(loadingObserver)
        viewModel.getRelations().observeForever(dataObserver)
        viewModel.getError().observeForever(errorObserver)
        viewModel.getGame().observeForever(gameObserver)
        viewModel.getScreenshot().observeForever(imageObserver)

        val flow = Flowable.create<List<GameRelation>>({ it.onNext(relations) }, BackpressureStrategy.MISSING)
        whenever(usecase.execute(any())).thenReturn(flow)

        val gameflow = Flowable.create<Game>({ it.onNext(game) }, BackpressureStrategy.MISSING)
        whenever(loadgameusecase.execute(any())).thenReturn(gameflow)
    }

    @Test
    fun fetchDataTriggersLoadingState() {
        // Act
        viewModel.loadRelationForGame(randomInt())

        verify(loadingObserver).onChanged(true)
    }

    @Test
    fun fetchDataTriggersLoadUseCase() {

        viewModel.loadRelationForGame(100)

        verify(loadgameusecase).execute(100)
    }

    @Test
    fun fetchDataTriggersLoadGameRelationUseCase() {

        viewModel.loadRelationForGame(100)

        verify(usecase).execute(game)
    }

    @Test
    fun fetchDataSuccessTriggersSuccessState() {
        viewModel.loadRelationForGame(10)

        verify(gameObserver).onChanged(game)
        verify(dataObserver).onChanged(relations)
        verify(loadingObserver).onChanged(false)
        verify(errorObserver).onChanged(null)
        verify(imageObserver).onChanged(anyString())
    }

    @Test
    fun fetchLoadGameErrorTriggersState() {
        val error = Throwable("foo")
        whenever(loadgameusecase.execute(any())).thenReturn(Flowable.error(error))

        viewModel.loadRelationForGame(anyInt())

        verify(gameObserver).onChanged(null)
        verify(dataObserver).onChanged(null)
        verify(loadingObserver).onChanged(false)
        verify(errorObserver).onChanged(error)
    }

    @Test
    fun fetchLoadGameRelationErrorTriggersState() {
        val error = Throwable("foo")
        whenever(usecase.execute(any())).thenReturn(Flowable.error(error))

        viewModel.loadRelationForGame(anyInt())

        verify(gameObserver).onChanged(game)
        verify(dataObserver).onChanged(null)
        verify(loadingObserver).onChanged(false)
        verify(errorObserver).onChanged(error)
    }
}