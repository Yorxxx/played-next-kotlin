package com.piticlistudio.playednext.ui.injection.builder

import com.piticlistudio.playednext.ui.game.search.GameSearchComponent
import com.piticlistudio.playednext.ui.game.search.GameSearchFragment
import com.piticlistudio.playednext.ui.game.search.GameSearchModule
import com.piticlistudio.playednext.ui.injection.PerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @PerFragment
    @ContributesAndroidInjector(modules = arrayOf(GameSearchModule::class))
    abstract fun bindSearchFragment(): GameSearchFragment
}