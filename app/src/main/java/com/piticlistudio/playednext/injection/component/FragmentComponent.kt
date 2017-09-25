package com.piticlistudio.playednext.injection.component

import com.piticlistudio.playednext.injection.PerFragment
import com.piticlistudio.playednext.injection.module.FragmentModule
import dagger.Subcomponent

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent