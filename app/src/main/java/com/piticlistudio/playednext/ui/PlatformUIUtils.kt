package com.piticlistudio.playednext.ui

import android.graphics.Color
import org.json.JSONObject
import java.io.InputStream
import java.nio.charset.Charset
import javax.inject.Inject

class PlatformUIUtils @Inject constructor(val colorsMap: HashMap<String, Int>, val acronymsMap: HashMap<String, String>) {

    fun getAcronym(platformName: String): String {
        return acronymsMap.get(platformName.toUpperCase()).takeIf { it != null } ?: acronymsMap.get(platformName).takeIf { it != null } ?: platformName
    }

    fun getColor(platformName: String): Int {
        return colorsMap.get(platformName.toUpperCase()).takeIf { it != null } ?: colorsMap.get(platformName).takeIf { it != null } ?: Color.BLACK
    }

    class Builder {
        private var colorsMap: HashMap<String, Int>? = null
        private var acronymsMap: HashMap<String, String>? = null
        private var inputStream: InputStream? = null

        fun inputstream(input: InputStream) = apply { this.inputStream = input }
        fun colorsmap(colors: HashMap<String, Int>) = apply { this.colorsMap = colors }
        fun acronymsmap(acronyms: HashMap<String, String>) = apply { this.acronymsMap = acronyms }

        fun build(): PlatformUIUtils {

            if (colorsMap != null && acronymsMap != null) {
                return PlatformUIUtils(colorsMap!!, acronymsMap!!)
            }
            inputStream?.let {
                if (colorsMap == null) {
                    colorsMap = HashMap<String, Int>()
                }
                if (acronymsMap == null) {
                    acronymsMap = HashMap<String, String>()
                }
                parseJSON(loadJSON(inputStream!!))
                return PlatformUIUtils(colorsMap!!, acronymsMap!!)
            }
            throw RuntimeException("No input specified")
        }

        private fun loadJSON(input: InputStream): String {
            val size = input.available()
            val buffer = ByteArray(size)
            input.read(buffer)
            input.close()

            return String(buffer, Charset.forName("UTF-8"))
        }

        private fun parseJSON(json: String) {
            val values = JSONObject(json)
            val keys = values.keys()

            while (keys.hasNext()) {
                val key = keys.next()
                val value = values.getJSONObject(key)
                val color = value.getString("color")
                val acronym = value.getString("acronym")
                colorsMap?.let {
                    if (!it.containsKey(key)) {
                        it.put(key.toUpperCase(), Color.parseColor(color))
                    }
                }
                acronymsMap?.let {
                    if (!it.containsKey(key)) {
                        it.put(key.toUpperCase(), acronym)
                    }
                }
            }
        }
    }
}