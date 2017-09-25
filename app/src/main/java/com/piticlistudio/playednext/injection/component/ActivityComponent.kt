package com.piticlistudio.playednext.injection.component

import com.piticlistudio.playednext.injection.PerActivity
import com.piticlistudio.playednext.injection.module.ActivityModule
import com.piticlistudio.playednext.features.base.BaseActivity
import com.piticlistudio.playednext.features.detail.DetailActivity
import com.piticlistudio.playednext.features.main.MainActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(baseActivity: BaseActivity)

    fun inject(mainActivity: MainActivity)

    fun inject(detailActivity: DetailActivity)
}
