package com.piticlistudio.playednext.data.repository

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.repository.datasource.PlaylistDatasourceRepository
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.factory.PlaylistFactory.Factory.makePlaylist
import io.reactivex.Flowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PlaylistRepositoryImplTest {

    private lateinit var repositoryImpl: PlaylistRepositoryImpl
    private val localImpl: PlaylistDatasourceRepository = mock()
    private val findAllResults = randomListOf(randomInt(20)){ makePlaylist() }

    @BeforeEach
    internal fun setUp() {
        reset(localImpl)
        repositoryImpl = PlaylistRepositoryImpl(localImpl)
        whenever(localImpl.findAll()).thenReturn(Flowable.defer { Flowable.just(findAllResults) })
    }

    @Test
    fun `loadAll() should request to load all playlists from local repository`() {

        repositoryImpl.loadAll().test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(findAllResults)
    }
}