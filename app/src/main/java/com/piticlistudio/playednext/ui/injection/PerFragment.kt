package com.piticlistudio.playednext.ui.injection

import javax.inject.Scope

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the Fragment to be memorised in the
 * correct component.
 */
@Scope @Retention(AnnotationRetention.RUNTIME) annotation class PerFragment