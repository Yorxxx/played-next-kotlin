package com.piticlistudio.playednext.injection.component

import com.piticlistudio.playednext.injection.ConfigPersistent
import com.piticlistudio.playednext.injection.module.ActivityModule
import com.piticlistudio.playednext.injection.module.FragmentModule
import com.piticlistudio.playednext.features.base.BaseActivity
import com.piticlistudio.playednext.features.base.BaseFragment
import dagger.Component

/**
 * A dagger component that will live during the lifecycle of an Activity or Fragment but it won't
 * be destroy during configuration changes. Check [BaseActivity] and [BaseFragment] to
 * see how this components survives configuration changes.
 * Use the [ConfigPersistent] scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */
@ConfigPersistent
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface ConfigPersistentComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent

    fun fragmentComponent(fragmentModule: FragmentModule): FragmentComponent

}
