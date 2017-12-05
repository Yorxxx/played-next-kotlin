package com.piticlistudio.playednext.domain.model

data class GameImage constructor(val id: String, val url: String, val width: Int?, val height: Int?) {

    val mediumSizeUrl: String = "https:${clearHTTPPrefix(url)}".replace("t_thumb", "t_screenshot_med")
    val bigSizeUrl: String = "https:${clearHTTPPrefix(url)}".replace("t_thumb", "t_screenshot_big")
    val hugeSizeUrl: String = "https:${clearHTTPPrefix(url)}".replace("t_thumb", "t_screenshot_huge")

    private fun clearHTTPPrefix(input: String): String {
        return input.removePrefix("http:").removePrefix("https:")
    }
}