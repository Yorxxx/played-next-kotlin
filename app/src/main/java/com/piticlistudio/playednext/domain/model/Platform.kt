package com.piticlistudio.playednext.domain.model

/**
 * Representation of a Platform
 */
data class Platform(val id: Int, val name: String, val slug: String, val url: String?, val createdAt: Long, val updatedAt: Long)