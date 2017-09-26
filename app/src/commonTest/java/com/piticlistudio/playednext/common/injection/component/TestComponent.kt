package com.piticlistudio.playednext.common.injection.component

import com.piticlistudio.playednext.common.injection.module.ApplicationTestModule
import com.piticlistudio.playednext.ui.injection.component.ApplicationComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationTestModule::class))
interface TestComponent : ApplicationComponent