package com.piticlistudio.playednext.ui.gamerelation.detail

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
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
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class GameRelationDetailViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @JvmField
    @Rule
    val taskRule = InstantTaskExecutorRule()

    private lateinit var vm: GameRelationDetailViewModel

    @Mock lateinit var loadRelationsForGameUseCase: LoadRelationsForGameUseCase
    @Mock lateinit var loadGameUseCase: LoadGameUseCase
    @Mock private lateinit var observer: Observer<ViewState>

    private val relations = randomListOf(5) { makeGameRelation() }
    private val game = makeGame()

    @Before
    fun setUp() {
        game.images = listOf()
        MockitoAnnotations.initMocks(this)
        vm = GameRelationDetailViewModel(loadRelationsForGameUseCase, loadGameUseCase)
        vm.getCurrentState().observeForever(observer)

        val flow = Flowable.create<List<GameRelation>>({ it.onNext(relations) }, BackpressureStrategy.MISSING)
        whenever(loadRelationsForGameUseCase.execute(any())).thenReturn(flow)

        val gameflow = Flowable.create<Game>({ it.onNext(game) }, BackpressureStrategy.MISSING)
        whenever(loadGameUseCase.execute(any())).thenReturn(gameflow)
    }

    @Test
    fun requestsUsecasesWithCorrectParameters() {
        vm.loadRelationForGame(100)

        verify(loadGameUseCase).execute(100)
        verify(loadRelationsForGameUseCase).execute(game)
    }

    @Test
    fun showsSuccessViewState() {

        vm.loadRelationForGame(randomInt())

        val captor = argumentCaptor<ViewState>()
        verify(observer, times(3)).onChanged(captor.capture())

        assertTrue {
            captor.firstValue.isLoading && captor.firstValue.game == null && captor.firstValue.error == null
                    && captor.firstValue.showImage == null && captor.firstValue.relations.isEmpty()
        }

        assertTrue {
            !captor.lastValue.isLoading && captor.lastValue.game != null && captor.lastValue.game!!.equals(game)
                    && captor.lastValue.relations.equals(relations)
                    && captor.lastValue.error == null
        }
        captor.allValues.forEach {
            assertNull(it.error)
        }
    }

    @Test
    fun showsErrorViewStateWhenLoadingAGame() {

        val error = Throwable()
        whenever(loadGameUseCase.execute(any())).thenReturn(Flowable.error(error))
        vm.loadRelationForGame(randomInt())

        val captor = argumentCaptor<ViewState>()
        verify(observer, times(2)).onChanged(captor.capture())

        assertTrue {
            captor.firstValue.isLoading && captor.firstValue.game == null && captor.firstValue.error == null
                    && captor.firstValue.showImage == null && captor.firstValue.relations.isEmpty()
        }

        assertTrue {
            !captor.lastValue.isLoading && captor.lastValue.game == null && captor.lastValue.relations.isEmpty()
                    && captor.lastValue.error == error
        }
        captor.allValues.forEach {
            assertNull(it.game)
            assertTrue { it.relations.isEmpty() }
            assertNull(it.showImage)
        }
    }

    @Test
    fun showsErrorViewStateWhenLoadingRelations() {

        val error = Throwable()
        whenever(loadRelationsForGameUseCase.execute(any())).thenReturn(Flowable.error(error))
        vm.loadRelationForGame(randomInt())

        val captor = argumentCaptor<ViewState>()
        verify(observer, times(3)).onChanged(captor.capture())

        assertTrue {
            captor.firstValue.isLoading && captor.firstValue.game == null && captor.firstValue.error == null
                    && captor.firstValue.showImage == null && captor.firstValue.relations.isEmpty()
        }

        assertTrue {
            !captor.lastValue.isLoading && captor.lastValue.game == game && captor.lastValue.relations.isEmpty()
                    && captor.lastValue.error == error
        }
        captor.allValues.forEach {
            assertTrue { it.relations.isEmpty() }
        }
    }
}