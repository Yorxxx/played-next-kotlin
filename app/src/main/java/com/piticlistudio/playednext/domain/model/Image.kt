package com.piticlistudio.playednext.domain.model

class GameImage constructor(val id: String, val url: String, val width: Int? = 0, val height: Int? = 0): BaseImage() {

    val mediumSizeUrl: String = "https:${clearHTTPPrefix(url)}".replace("t_thumb", "t_screenshot_med")
    val bigSizeUrl: String = "https:${clearHTTPPrefix(url)}".replace("t_thumb", "t_screenshot_big")
    val hugeSizeUrl: String = "https:${clearHTTPPrefix(url)}".replace("t_thumb", "t_screenshot_huge")
}