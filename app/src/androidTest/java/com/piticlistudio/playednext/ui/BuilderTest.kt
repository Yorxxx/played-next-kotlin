package com.piticlistudio.playednext.ui

import android.graphics.Color
import android.support.test.InstrumentationRegistry
import junit.framework.Assert.assertEquals
import org.junit.Test

internal class BuilderTest {

    @Test
    fun createsInstanceFromMaps() {
        val platformname = "Nintendo GameCube"
        val colors = HashMap<String, Int>().apply { put(platformname, Color.CYAN) }
        val acronyms = HashMap<String, String>().apply { put(platformname, "NGC") }

        val utils = PlatformUIUtils.Builder().acronymsmap(acronyms).colorsmap(colors).build()

        assertEquals(Color.CYAN, utils.getColor(platformname))
        assertEquals("NGC", utils.getAcronym(platformname))
    }

    @Test
    fun createsInstanceFromInput() {
        val platformname = "Wii"
        val input = InstrumentationRegistry.getTargetContext().assets.open("platformsui.json")

        val utils = PlatformUIUtils.Builder().inputstream(input).build()

        assertEquals("Wii", utils.getAcronym(platformname))
        assertEquals(Color.parseColor("#d9d9d9"), utils.getColor(platformname))
    }

    @Test
    fun shouldCombineAcronymsMapAndColorsFromInputStream() {
        val platformname = "Nintendo 64"
        val input = InstrumentationRegistry.getTargetContext().assets.open("platformsui.json")
        val acronyms = HashMap<String, String>().apply { put(platformname, "U64") }

        val utils = PlatformUIUtils.Builder().acronymsmap(acronyms).inputstream(input).build()

        assertEquals("U64", utils.getAcronym(platformname))
        assertEquals(-4079919, utils.getColor(platformname))
    }

    @Test
    fun shouldCombineColorsMapAndAcronymsFromInputStream() {
        val platformname = "Nintendo 64"
        val input = InstrumentationRegistry.getTargetContext().assets.open("platformsui.json")
        val colors = HashMap<String, Int>().apply { put(platformname, Color.BLUE) }

        val utils = PlatformUIUtils.Builder().colorsmap(colors).inputstream(input).build()

        assertEquals("n64", utils.getAcronym(platformname))
        assertEquals(Color.BLUE, utils.getColor(platformname))
    }

    @Test
    fun shouldMergeAcronymsMapAndAcronymsFromInputStream() {
        val platformname = "Nintendo 64"
        val input = InstrumentationRegistry.getTargetContext().assets.open("platformsui.json")
        val acronyms = HashMap<String, String>().apply { put("viruta", "vrt") }

        val utils = PlatformUIUtils.Builder().acronymsmap(acronyms).inputstream(input).build()

        assertEquals("n64", utils.getAcronym(platformname))
        assertEquals("vrt", utils.getAcronym("viruta"))
    }

    @Test
    fun shouldMergeColorsMapAndColorsFromInputStream() {
        val platformname = "Nintendo 64"
        val input = InstrumentationRegistry.getTargetContext().assets.open("platformsui.json")
        val colors = HashMap<String, Int>().apply { put("viruta", Color.parseColor("#666699")) }

        val utils = PlatformUIUtils.Builder().colorsmap(colors).inputstream(input).build()

        assertEquals(Color.parseColor("#C1BED1"), utils.getColor(platformname))
        assertEquals(Color.parseColor("#666699"), utils.getColor("viruta"))
    }
}