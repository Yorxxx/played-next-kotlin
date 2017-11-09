package com.piticlistudio.playednext.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.ui.game.search.GameSearchFragment
import com.piticlistudio.playednext.ui.gamerelation.detail.GameRelationDetailFragment
import com.piticlistudio.playednext.util.ext.setContentFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class FooActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.game_search_fragment)
        setContentFragment(android.R.id.content, { GameSearchFragment() })
    }

    override fun supportFragmentInjector() = fragmentInjector
}