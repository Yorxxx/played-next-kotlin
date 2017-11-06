package com.piticlistudio.playednext.features

import android.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.features.gamerelation.load.GameRelationLoadViewModel
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

        val viewmodel = ViewModelProviders.of(this).get(GameRelationLoadViewModel::class.java)
        viewmodel.loading.observe(this, Observer<Boolean> {
            Log.d("FooActivity", "Is loading ${it}")
        })

        viewmodel.error.observe(this, Observer<Throwable?> {
            it?.let {
                Log.d("FooActivity", "Showing error ${it}")
            }
        })

        viewmodel.relation.observe(this, Observer<GameRelation?> {
            it?.let {
                Log.d("FooActivity", "Showing relation ${it}")
            }
        })
    }

    override fun fragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }
}