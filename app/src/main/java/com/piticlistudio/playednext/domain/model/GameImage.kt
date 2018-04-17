package com.piticlistudio.playednext.domain.model

class GameImage constructor(url: String, width: Int? = 0, height: Int? = 0, val gameId: Int) : Image(url, width, height)