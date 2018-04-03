package com.piticlistudio.playednext.ui.game.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.arch.paging.TiledDataSource
import android.net.NetworkInfo
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.util.NoNetworkAvailableException
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import rx.subjects.BehaviorSubject
import java.net.UnknownHostException
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Test cases for GameSearchViewModel
 */
internal class GameSearchViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @JvmField
    @Rule
    val taskRule = InstantTaskExecutorRule()

    private lateinit var viewModel: GameSearchViewModel

    @Mock private lateinit var searchGamesUseCase: SearchGamesUseCase
    private var connectivity = BehaviorSubject.create<Connectivity>()

    @Mock private lateinit var loadingObserver: Observer<Boolean>
    @Mock private lateinit var errorObserver: Observer<Exception?>
    @Mock private lateinit var currentConnection: Connectivity
    @Mock private lateinit var provider: TiledDataSource<Game>
    private val games = randomListOf(30) { makeGame() }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = GameSearchViewModel(searchGamesUseCase, connectivity)
        viewModel.provider = provider
        viewModel.getLoading().observeForever(loadingObserver)
        //viewModel.getData().observeForever(dataObserver)
        viewModel.getError().observeForever(errorObserver)
        val flow = Flowable.create<List<Game>>({ it.onNext(games) }, BackpressureStrategy.MISSING)
        whenever(searchGamesUseCase.execute(any())).thenReturn(flow)
    }

    @Test
    fun initHidesLoading() {
        verify(loadingObserver).onChanged(false)
    }

    @Test
    fun hidesErrorWhenNoConnectivityIssues() {
        whenever(currentConnection.state).thenReturn(NetworkInfo.State.CONNECTED)
        connectivity.onNext(currentConnection)

        verify(errorObserver).onChanged(null)
    }

    @Test
    fun showsErrorWhenConnectivityIsLost() {
        whenever(currentConnection.state).thenReturn(NetworkInfo.State.DISCONNECTED)
        connectivity.onNext(currentConnection)
        val error = argumentCaptor<Exception>()

        // Act
        verify(errorObserver).onChanged(error.capture())
        assertNotNull(error)
        assertTrue { error.firstValue is NoNetworkAvailableException }
    }

    @Test
    fun showsLoadingWhenChangingQuery() {

        viewModel.setQueryFilter("foo")

        verify(loadingObserver).onChanged(true)
    }

    @Test
    fun hidesLoadingWhenQueryEnds() {
        reset(loadingObserver)
        viewModel.onSearchCompleted("foo", 1, 2)

        verify(loadingObserver).onChanged(false)
    }

    @Test
    fun showsNoNetworkErrorWhenUnknownHostException() {
        val error = argumentCaptor<Exception>()
        viewModel.onSearchFailed(UnknownHostException())

        verify(errorObserver).onChanged(error.capture())
        assertNotNull(error)
        assertTrue { error.firstValue is NoNetworkAvailableException }
    }

    @Test
    fun showsNoNetworkErrorWhenUnknownHostExceptionWithinRuntimeException() {
        val error = argumentCaptor<Exception>()
        viewModel.onSearchFailed(RuntimeException(UnknownHostException()))

        verify(errorObserver).onChanged(error.capture())
        assertNotNull(error)
        assertTrue { error.firstValue is NoNetworkAvailableException }
    }

    @Test
    fun showsErrorWhenUnknownException() {
        val error = argumentCaptor<Exception>()
        val exception = Exception()
        viewModel.onSearchFailed(exception)

        verify(errorObserver).onChanged(error.capture())
        assertNotNull(error)
        assertTrue { error.firstValue == exception }
    }

    @Test
    fun restoresProviderLoadingWhenConnectionIsRestored() {
        // Arrange
        viewModel.setQueryFilter("foo")
        viewModel.onSearchCompleted("foo", 24, 12)

        // Act
        reset(loadingObserver)
        whenever(currentConnection.state).thenReturn(NetworkInfo.State.DISCONNECTED)
        connectivity.onNext(currentConnection)
        whenever(currentConnection.state).thenReturn(NetworkInfo.State.CONNECTED)
        connectivity.onNext(currentConnection)

        // Assert
        verify(loadingObserver).onChanged(true)
    }
}