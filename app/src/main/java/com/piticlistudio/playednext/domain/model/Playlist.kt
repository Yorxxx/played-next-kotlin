package com.piticlistudio.playednext.domain.model

/**
 * Representation of a playlist
 * A playlist is defined by its [name], and includes an optional [description], a color attribute
 * and a list of [games]
 * For example, a Playlist with [name] "Backlog", could containg a list of games not played
 */
data class Playlist(val name: String, val description: String?, val color: Int, val games: List<Game> = listOf())