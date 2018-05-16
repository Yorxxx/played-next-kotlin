package com.piticlistudio.playednext.ui.playlists.overview

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.domain.interactor.playlists.LoadAllPlaylistsUseCase
import com.piticlistudio.playednext.domain.model.Playlist
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.factory.PlaylistFactory.Factory.makePlaylist
import com.piticlistudio.playednext.ui.playlists.overview.mapper.PlaylistsOverviewModelMapper
import com.piticlistudio.playednext.ui.playlists.overview.model.PlaylistsOverviewModel
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.ReplaySubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

    private val viewStateObserver: Observer<ViewState> = mock()
    private val useCaseSubject = ReplaySubject.create<List<Playlist>>()

    @Before
    internal fun setUp() {
        reset(useCase, mapper)
        vm = PlaylistsOverviewViewModel(useCase, mapper)

        vm = PlaylistsOverviewViewModel(useCase, mapper)
        vm.getViewState().observeForever(viewStateObserver)
    }

    @Test
    fun `should emit mapped result from usecase`() {

        val data = randomListOf { makePlaylist() }
        val mapResponse = PlaylistsOverviewModel("a", 1, 1)
        ArrangeBuilder().withMapperResponse(mapResponse)
                .withUseCaseResponse(data)

        vm.start()

        // Assert
        inOrder(mapper, useCase, viewStateObserver) {
            verify(useCase).execute()
            verify(viewStateObserver).onChanged(argForWhich {
                isLoading && error == null && items.isEmpty()
            })
            verify(mapper, times(data.size)).mapIntoPresentationModel(argForWhich {
                data.contains(this)
            })
            verify(viewStateObserver).onChanged(argForWhich {
                !isLoading && error == null && items.isEmpty()
            })
            verify(viewStateObserver).onChanged(argForWhich {
                !isLoading && error == null && items.size == data.size && items.all { it == mapResponse } && !showEmptyView && showTitle
            })
        }
    }

    @Test
    fun `should emit empty view when there are no playlists`() {

        val data = emptyList<Playlist>()
        ArrangeBuilder().withUseCaseResponse(data)

        vm.start()

        // Assert
        inOrder(mapper, useCase, viewStateObserver) {
            verify(useCase).execute()
            verify(viewStateObserver).onChanged(argForWhich {
                isLoading && error == null && items.isEmpty()
            })
            verifyZeroInteractions(mapper)
            verify(viewStateObserver).onChanged(argForWhich {
                !isLoading && error == null && items.isEmpty() && !showEmptyView && showTitle
            })
            verify(viewStateObserver).onChanged(argForWhich {
                !isLoading && error == null && items.isEmpty() && showEmptyView && !showTitle
            })
        }
    }

    @Test
    fun `should show error when usecase fails`() {

        val error = Throwable("foo")
        ArrangeBuilder().withUseCaseResponse(error)

        vm.start()

        // Assert
        inOrder(mapper, useCase, viewStateObserver) {
            verify(useCase).execute()
            verify(viewStateObserver).onChanged(argForWhich {
                isLoading && this.error == null && items.isEmpty()
            })
            verify(viewStateObserver).onChanged(argForWhich {
                !isLoading && this.error == null && items.isEmpty()
            })
            verifyZeroInteractions(mapper)
            verify(viewStateObserver).onChanged(argForWhich {
                !isLoading && this.error == error.message && items.isEmpty() && !showEmptyView && showTitle
            })
        }
    }

    private inner class ArrangeBuilder {

        fun withMapperResponse(response: PlaylistsOverviewModel): ArrangeBuilder {
            doAnswer { response }.whenever(mapper).mapIntoPresentationModel(any())
            return this
        }

        fun withUseCaseResponse(response: List<Playlist>): ArrangeBuilder {
            whenever(useCase.execute()).thenReturn(useCaseSubject.toFlowable(BackpressureStrategy.DROP))
            useCaseSubject.onNext(response)
            return this
        }

        fun withUseCaseResponse(response: Throwable): ArrangeBuilder {
            whenever(useCase.execute()).thenReturn(Flowable.error(response))
            return this
        }
    }
}