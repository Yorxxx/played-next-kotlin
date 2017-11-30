package com.piticlistudio.playednext.ui.gamerelation.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.piticlistudio.playednext.util.ext.setContentFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class GameRelationDetailActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentFragment(android.R.id.content, { GameRelationDetailFragment.newInstance(intent.getIntExtra("id", 0)) })
    }

    override fun supportFragmentInjector() = fragmentInjector
}