package com.piticlistudio.playednext.ui.gamerelation.detail

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombEntityResponse
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombGame
import com.piticlistudio.playednext.data.entity.room.RoomGame
import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.relation.LoadRelationsForGameUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.*

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
        vm.loadRelationForGame(game)

        verify(loadGameUseCase).execute(game.id)
        verify(loadRelationsForGameUseCase).execute(game)
    }

    @Test
    fun showsSuccessViewState() {

        vm.loadRelationForGame(game)

        val captor = argumentCaptor<ViewState>()
        verify(observer, times(3)).onChanged(captor.capture())

        with(captor.firstValue) {
            assertTrue(isLoading)
            assertNotNull(game)
            assertNull(error)
            assertNull(showImage)
            assertTrue(relations.isEmpty())
        }

        with(captor.lastValue) {
            assertFalse(isLoading)
            assertNotNull(game)
            assertNull(error)
            assertFalse(relations.isEmpty())
        }

        captor.allValues.forEach {
            assertNull(it.error)
        }
    }

    @Test
    fun showsErrorViewStateWhenLoadingAGame() {

        val error = Throwable()
        val flowable = Flowable.create<Game>({
            it.onError(error)
        }, BackpressureStrategy.MISSING)
        whenever(loadGameUseCase.execute(any())).thenReturn(flowable)
        vm.loadRelationForGame(game)

        val captor = argumentCaptor<ViewState>()
        verify(observer, times(2)).onChanged(captor.capture())

        with(captor.firstValue) {
            assertTrue(isLoading)
            assertNotNull(game)
            assertNull(this.error)
            assertNull(showImage)
            assertTrue(relations.isEmpty())
        }

        with(captor.lastValue) {
            assertFalse(isLoading)
            assertNotNull(game)
            assertNotNull(error)
            assertTrue(relations.isEmpty())
        }
    }

    @Test
    fun showsErrorViewStateWhenLoadingRelations() {

        val error = Throwable()
        val flowable = Flowable.create<Game>({
            it.onError(error)
        }, BackpressureStrategy.MISSING)
        whenever(loadGameUseCase.execute(any())).thenReturn(flowable)

        vm.loadRelationForGame(game)

        val captor = argumentCaptor<ViewState>()
        verify(observer, times(2)).onChanged(captor.capture())

        with(captor.firstValue) {
            assertTrue(isLoading)
            assertNotNull(game)
            assertNull(this.error)
            assertNull(showImage)
            assertTrue(relations.isEmpty())
        }

        with(captor.lastValue) {
            assertFalse(isLoading)
            assertNotNull(game)
            assertNotNull(error)
            assertNull(showImage)
            assertTrue(relations.isEmpty())
        }

        captor.allValues.forEach {
            assertTrue { it.relations.isEmpty() }
        }
    }
}