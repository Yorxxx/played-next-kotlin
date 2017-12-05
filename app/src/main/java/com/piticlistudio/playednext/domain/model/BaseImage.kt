package com.piticlistudio.playednext.domain.model

abstract class BaseImage {

    protected fun clearHTTPPrefix(input: String): String {
        return input.removePrefix("http:").removePrefix("https:")
    }
}