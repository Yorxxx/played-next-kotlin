package com.piticlistudio.playednext.domain.model

data class GameImage constructor(val id: String, val url: String, val width: Int?, val height: Int?) {

    val mediumSizeUrl: String = "http:${url}".replace("t_thumb", "t_screenshot_med")
    val bigSizeUrl: String = "http:${url}".replace("t_thumb", "t_screenshot_big")
    val hugeSizeUrl: String = "http:${url}".replace("t_thumb", "t_screenshot_huge")
}