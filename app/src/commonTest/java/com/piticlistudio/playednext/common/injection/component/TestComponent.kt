package com.piticlistudio.playednext.common.injection.component

import com.piticlistudio.playednext.common.injection.module.ApplicationTestModule
import com.piticlistudio.playednext.ui.injection.component.ApplicationComponent
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, ApplicationTestModule::class))
interface TestComponent : ApplicationComponent