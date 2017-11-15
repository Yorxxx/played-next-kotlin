package com.piticlistudio.playednext.util

/**
 * A Collection of exceptions that are useful across the app and JDK does not seem to provide in any
 * standar way.
 */
class NoNetworkAvailableException(msg: String? = "No Internet connection available", cause: Throwable? = null): Exception(msg, cause)