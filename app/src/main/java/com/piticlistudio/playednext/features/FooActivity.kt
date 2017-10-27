package com.piticlistudio.playednext.features

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.ui.game.load.GameLoadFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import javax.inject.Inject

class FooActivity : AppCompatActivity(), HasFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_search_fragment)
        fragmentManager.beginTransaction()
                .add(android.R.id.content, GameLoadFragment())
                .commit()
    }

    override fun fragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }
}