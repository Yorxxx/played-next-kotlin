package com.piticlistudio.playednext.ui.gamerelation.overview.mapper

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.factory.GameRelationFactory.Factory.makeGameRelation
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class RelationOverviewModelMapperTest {

    private lateinit var mapper: RelationOverviewModelMapper

    @Before
    fun setUp() {
        mapper = RelationOverviewModelMapper(InstrumentationRegistry.getTargetContext())
    }

    @Test
    fun shouldMapToPresentationModelWhenStatusIsBeaten() {

        val count = 18
        val status = GameRelationStatus.BEATEN
        val data = randomListOf(count) { makeGameRelation(status) }

        val result = mapper.mapIntoPresentationModel(data, status)

        Assert.assertNotNull(result)
        Assert.assertEquals(18, result.count)
        Assert.assertEquals(InstrumentationRegistry.getTargetContext().getString(R.string.gamerelation_beaten_status), result.name)
        Assert.assertEquals(R.drawable.shape_gradient_beaten, result.background)
        Assert.assertEquals(R.color.gamerelation_beaten_color, result.color)
    }

    @Test
    fun shouldMapToPresentationModelWhenStatusIsUnplayed() {

        val count = randomInt()
        val status = GameRelationStatus.UNPLAYED
        val data = randomListOf(count) { makeGameRelation(status) }

        val result = mapper.mapIntoPresentationModel(data, status)

        Assert.assertNotNull(result)
        Assert.assertEquals(count, result.count)
        Assert.assertEquals(InstrumentationRegistry.getTargetContext().getString(R.string.gamerelation_backlog_status), result.name)
        Assert.assertEquals(R.drawable.shape_gradient_backlog, result.background)
        Assert.assertEquals(R.color.gamerelation_backlog_color, result.color)
    }

    @Test
    fun shouldMapToPresentationModelWhenStatusIsPlaying() {

        val count = randomInt()
        val status = GameRelationStatus.PLAYING
        val data = randomListOf(count) { makeGameRelation(status) }

        val result = mapper.mapIntoPresentationModel(data, status)

        Assert.assertNotNull(result)
        Assert.assertEquals(count, result.count)
        Assert.assertEquals(InstrumentationRegistry.getTargetContext().getString(R.string.gamerelation_playing_status), result.name)
        Assert.assertEquals(R.drawable.shape_gradient_playing, result.background)
        Assert.assertEquals(R.color.gamerelation_current_color, result.color)
    }

    @Test
    fun shouldMapToPresentationModelWhenStatusIsCompleted() {

        val count = randomInt()
        val status = GameRelationStatus.COMPLETED
        val data = randomListOf(count) { makeGameRelation(status) }

        val result = mapper.mapIntoPresentationModel(data, status)

        Assert.assertNotNull(result)
        Assert.assertEquals(count, result.count)
        Assert.assertEquals(InstrumentationRegistry.getTargetContext().getString(R.string.gamerelation_completed_status), result.name)
        Assert.assertEquals(R.drawable.shape_gradient_completed, result.background)
        Assert.assertEquals(R.color.gamerelation_completed_color, result.color)
    }

    @Test
    fun shouldMapToPresentationModelWhenStatusIsPlayed() {

        val count = randomInt()
        val status = GameRelationStatus.PLAYED
        val data = randomListOf(count) { makeGameRelation(status) }

        val result = mapper.mapIntoPresentationModel(data, status)

        Assert.assertNotNull(result)
        Assert.assertEquals(count, result.count)
        Assert.assertEquals(InstrumentationRegistry.getTargetContext().getString(R.string.gamerelation_played_status), result.name)
        Assert.assertEquals(R.drawable.shape_gradient_played, result.background)
        Assert.assertEquals(R.color.gamerelation_played_color, result.color)
    }
}