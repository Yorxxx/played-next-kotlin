package com.piticlistudio.playednext.ui.injection.builder

import com.piticlistudio.playednext.ui.game.load.GameLoadFragment
import com.piticlistudio.playednext.ui.game.load.GameLoadModule
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

    @PerFragment
    @ContributesAndroidInjector(modules = arrayOf(GameLoadModule::class))
    abstract fun bindLoadGameFragment(): GameLoadFragment
}