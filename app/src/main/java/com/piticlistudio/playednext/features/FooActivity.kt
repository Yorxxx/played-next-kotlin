package com.piticlistudio.playednext.features

import android.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.features.game.load.GameDetailViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import javax.inject.Inject


class FooActivity : AppCompatActivity(), HasFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_search_fragment)
        /*fragmentManager.beginTransaction()
                .add(android.R.id.content, GameLoadFragment())
                .commit()*/

        val viewmodel = ViewModelProviders.of(this, mViewModelFactory).get(GameDetailViewModel::class.java)
        viewmodel.getLoadingStatus().observe(this, Observer {
            Log.d("FooActivity", "Is loading ${it}")
        })
        viewmodel.getGameStatus().observe(this, Observer {
            when (it) {
                null -> { Log.d("FooActivity", "No game available") }
                else -> { Log.d("FooActivity", "Retrieved game ${it}")
                }
            }
        })
        if (savedInstanceState == null)
            viewmodel.load(2001)
    }

    override fun fragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }
}