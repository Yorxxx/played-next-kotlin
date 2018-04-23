package com.piticlistudio.playednext.ui.playlists.overview

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.domain.interactor.playlists.LoadAllPlaylistsUseCase
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.domain.model.Playlist
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.factory.PlaylistFactory.Factory.makePlaylist
import com.piticlistudio.playednext.ui.playlists.overview.mapper.PlaylistsOverviewModelMapper
import com.piticlistudio.playednext.ui.playlists.overview.model.PlaylistsOverviewModel
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class PlaylistsOverviewViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @JvmField
    @Rule
    val taskRule = InstantTaskExecutorRule()

    private lateinit var vm: PlaylistsOverviewViewModel
    private val useCase: LoadAllPlaylistsUseCase = mock()
    private val mapper: PlaylistsOverviewModelMapper = mock()

    val overviewObserver: Observer<List<PlaylistsOverviewModel>> = mock()
    val loadingObserver: Observer<Boolean> = mock()
    val errorObserver: Observer<String> = mock()
    val useCaseResult = randomListOf { makePlaylist() }

    val useCaseSubject = PublishSubject.create<List<Playlist>>()

    @Before
    internal fun setUp() {
        reset(useCase, mapper)
        vm = PlaylistsOverviewViewModel(useCase, mapper)
        doAnswer {
            val playlist = it.arguments[0] as Playlist
            return@doAnswer PlaylistsOverviewModel(playlist.name, playlist.games.size, playlist.color)
        }.whenever(mapper).mapIntoPresentationModel(any())

        whenever(useCase.execute()).thenReturn(useCaseSubject.toFlowable(BackpressureStrategy.MISSING))

        vm = PlaylistsOverviewViewModel(useCase, mapper)
        vm.getLoading().observeForever(loadingObserver)
        vm.getError().observeForever(errorObserver)
        vm.getOverview().observeForever(overviewObserver)

        vm.start()

        useCaseSubject.onNext(useCaseResult)
    }

    @Test
    fun `should execute usecase`() {
        verify(useCase).execute()
    }

    @Test
    fun `should request mapper to map usecase response`() {
        verify(mapper, times(useCaseResult.size)).mapIntoPresentationModel(any())
        useCaseResult.forEach {
            verify(mapper).mapIntoPresentationModel(it)
        }
    }

    @Test
    fun `should show loading and hide it after emission`() {
        val captor = argumentCaptor<Boolean>()
        verify(loadingObserver, times(2)).onChanged(captor.capture())

        assertEquals(2, captor.allValues.size)
        assertTrue(captor.firstValue)
        assertFalse(captor.lastValue)
    }

    @Test
    fun `should emit overview model`() {
        val captor = argumentCaptor<List<PlaylistsOverviewModel>>()
        verify(overviewObserver).onChanged(captor.capture())

        assertEquals(1, captor.allValues.size)

        val anotherUseCaseResult = randomListOf { makePlaylist() }
        useCaseSubject.onNext(anotherUseCaseResult)

        verify(overviewObserver, times(2)).onChanged(captor.capture())
    }

    @Test
    fun `stop() should dispose from usecase`() {

        val captor = argumentCaptor<List<PlaylistsOverviewModel>>()
        verify(overviewObserver).onChanged(captor.capture())
        verify(loadingObserver, times(2)).onChanged(argumentCaptor<Boolean>().capture())
        verify(errorObserver).onChanged(argumentCaptor<String>().capture())

        assertEquals(1, captor.allValues.size)

        // Act
        vm.stop()

        val anotherUseCaseResult = randomListOf { makePlaylist() }
        useCaseSubject.onNext(anotherUseCaseResult)
        verifyNoMoreInteractions(overviewObserver)
        verifyNoMoreInteractions(loadingObserver)
        verifyNoMoreInteractions(errorObserver)
    }
}