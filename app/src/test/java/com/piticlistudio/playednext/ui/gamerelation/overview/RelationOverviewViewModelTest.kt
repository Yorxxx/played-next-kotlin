package com.piticlistudio.playednext.ui.gamerelation.overview

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.domain.interactor.relation.LoadRelationsWithStatusUseCase
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.ui.gamerelation.overview.mapper.RelationOverviewModelMapper
import com.piticlistudio.playednext.ui.gamerelation.overview.model.RelationOverviewModel
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class RelationOverviewViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @JvmField
    @Rule
    val taskRule = InstantTaskExecutorRule()

    private lateinit var vm: RelationOverviewViewModel
    val usecase: LoadRelationsWithStatusUseCase = mock()
    val mapper: RelationOverviewModelMapper = mock()

    val overviewObserver: Observer<List<RelationOverviewModel>> = mock()
    val loadingObserver: Observer<Boolean> = mock()
    val errorObserver: Observer<String> = mock()
    val completed: List<GameRelation> = randomListOf(5) { makeGameRelation(status = GameRelationStatus.COMPLETED) }
    val beaten: List<GameRelation> = randomListOf(10) { makeGameRelation(status = GameRelationStatus.BEATEN) }
    val playing: List<GameRelation> = randomListOf(2) { makeGameRelation(status = GameRelationStatus.PLAYING) }
    val playing2: List<GameRelation> = randomListOf(3) { makeGameRelation(status = GameRelationStatus.PLAYING) }
    val played: List<GameRelation> = randomListOf(12) { makeGameRelation(status = GameRelationStatus.PLAYED) }
    val backlog: List<GameRelation> = randomListOf(52) { makeGameRelation(status = GameRelationStatus.UNPLAYED) }

    val playingSubject = PublishSubject.create<List<GameRelation>>()

    @Before
    fun setUp() {
        reset(usecase, overviewObserver, loadingObserver, errorObserver, mapper)
        val completedFlow = Flowable.create<List<GameRelation>>({
            it.onNext(completed)
        }, BackpressureStrategy.MISSING)
        whenever(usecase.execute(GameRelationStatus.COMPLETED)).thenReturn(completedFlow)

        val beatenFlow = Flowable.create<List<GameRelation>>({ it.onNext(beaten) }, BackpressureStrategy.MISSING)
        whenever(usecase.execute(GameRelationStatus.BEATEN)).thenReturn(beatenFlow)

        whenever(usecase.execute(GameRelationStatus.PLAYING)).thenReturn(playingSubject.toFlowable(BackpressureStrategy.MISSING))

        val playedFlow = Flowable.create<List<GameRelation>>({ it.onNext(played) }, BackpressureStrategy.MISSING)
        whenever(usecase.execute(GameRelationStatus.PLAYED)).thenReturn(playedFlow)

        val backlogFlow = Flowable.create<List<GameRelation>>({ it.onNext(backlog) }, BackpressureStrategy.MISSING)
        whenever(usecase.execute(GameRelationStatus.UNPLAYED)).thenReturn(backlogFlow)

        doAnswer {
            val requestedList = it.arguments[0] as List<*>
            val requestedStatus = it.arguments[1] as GameRelationStatus
            return@doAnswer RelationOverviewModel(requestedStatus.name, requestedList.size, null, 0)
        }.whenever(mapper).mapIntoPresentationModel(any(), any())

        vm = RelationOverviewViewModel(usecase, mapper)
        vm.getLoading().observeForever(loadingObserver)
        vm.getError().observeForever(errorObserver)
        vm.getOverview().observeForever(overviewObserver)

        vm.start()

        playingSubject.onNext(playing)
    }

    @Test
    fun emitsError() {
        reset(overviewObserver, loadingObserver, errorObserver)

        val error = Throwable("foo")
        val flow = Flowable.create<List<GameRelation>>({ it.onError(error) }, BackpressureStrategy.MISSING)
        whenever(usecase.execute(GameRelationStatus.UNPLAYED)).thenReturn(flow)

        // Act
        vm.start()

        // Assert
        val captor = argumentCaptor<Boolean>()
        verify(loadingObserver, times(2)).onChanged(captor.capture())
        verify(errorObserver).onChanged("foo")
        verify(overviewObserver, never()).onChanged(any())
        assertEquals(2, captor.allValues.size)
        assertTrue(captor.firstValue)
        assertFalse(captor.lastValue)
    }

    @Test
    fun requestsUseCaseWithEachAvailableStatus() {
        verify(usecase).execute(GameRelationStatus.COMPLETED)
        verify(usecase).execute(GameRelationStatus.PLAYED)
        verify(usecase).execute(GameRelationStatus.PLAYING)
        verify(usecase).execute(GameRelationStatus.BEATEN)
        verify(usecase).execute(GameRelationStatus.COMPLETED)
    }

    @Test
    fun requestsMapperWithEachAvailableStatus() {
        verify(mapper).mapIntoPresentationModel(backlog, GameRelationStatus.UNPLAYED)
        verify(mapper).mapIntoPresentationModel(playing, GameRelationStatus.PLAYING)
        verify(mapper).mapIntoPresentationModel(played, GameRelationStatus.PLAYED)
        verify(mapper).mapIntoPresentationModel(beaten, GameRelationStatus.BEATEN)
        verify(mapper).mapIntoPresentationModel(completed, GameRelationStatus.COMPLETED)
    }

    @Test
    fun showsAndHidesLoading() {
        val captor = argumentCaptor<Boolean>()
        verify(loadingObserver, times(2)).onChanged(captor.capture())

        assertEquals(2, captor.allValues.size)
        assertTrue(captor.firstValue)
        assertFalse(captor.lastValue)
    }

    @Test
    fun showsOverview() {
        val captor = argumentCaptor<List<RelationOverviewModel>>()
        verify(overviewObserver, times(1)).onChanged(captor.capture())

        assertEquals(1, captor.allValues.size)
        with(captor.firstValue) {
            assertEquals(5, this.size)
            assertEquals("UNPLAYED", first().name)
            assertEquals(backlog.size, first().count)

            assertEquals("PLAYING", get(1).name)
            assertEquals(playing.size, get(1).count)

            assertEquals("PLAYED", get(2).name)
            assertEquals(played.size, get(2).count)

            assertEquals("BEATEN", get(3).name)
            assertEquals(beaten.size, get(3).count)

            assertEquals("COMPLETED", get(4).name)
            assertEquals(completed.size, get(4).count)
        }
    }

    @Test
    fun shouldEmitAgainWheneverAUseCaseChanges() {
        // Act
        playingSubject.onNext(playing2)

        val captor = argumentCaptor<List<RelationOverviewModel>>()
        verify(overviewObserver, times(2)).onChanged(captor.capture())

        assertEquals(2, captor.allValues.size)
        with(captor.firstValue) {
            assertEquals(5, this.size)
            assertEquals("UNPLAYED", first().name)
            assertEquals(backlog.size, first().count)

            assertEquals("PLAYING", get(1).name)
            assertEquals(playing.size, get(1).count)

            assertEquals("PLAYED", get(2).name)
            assertEquals(played.size, get(2).count)

            assertEquals("BEATEN", get(3).name)
            assertEquals(beaten.size, get(3).count)

            assertEquals("COMPLETED", get(4).name)
            assertEquals(completed.size, get(4).count)
        }
        with(captor.lastValue) {
            assertEquals(5, this.size)
            assertEquals("UNPLAYED", first().name)
            assertEquals(backlog.size, first().count)

            assertEquals("PLAYING", get(1).name)
            assertEquals(playing2.size, get(1).count)

            assertEquals("PLAYED", get(2).name)
            assertEquals(played.size, get(2).count)

            assertEquals("BEATEN", get(3).name)
            assertEquals(beaten.size, get(3).count)

            assertEquals("COMPLETED", get(4).name)
            assertEquals(completed.size, get(4).count)
        }
    }
}