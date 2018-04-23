package com.piticlistudio.playednext.domain.interactor.playlists

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.repository.PlaylistRepository
import com.piticlistudio.playednext.factory.DataFactory
import com.piticlistudio.playednext.factory.PlaylistFactory
import io.reactivex.Flowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class LoadAllPlaylistsUseCaseTest {

    private lateinit var useCase: LoadAllPlaylistsUseCase
    private val repository: PlaylistRepository = mock()
    private val loadAllResults = DataFactory.randomListOf(DataFactory.randomInt(20)) { PlaylistFactory.makePlaylist() }

    @BeforeEach
    internal fun setUp() {
        reset(repository)
        useCase = LoadAllPlaylistsUseCase(repository)
        whenever(repository.loadAll()).thenReturn(Flowable.defer { Flowable.just(loadAllResults) })
    }

    @Test
    fun `execute() should request to load all playlists from repository`() {

        useCase.execute().test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(loadAllResults)
    }
}