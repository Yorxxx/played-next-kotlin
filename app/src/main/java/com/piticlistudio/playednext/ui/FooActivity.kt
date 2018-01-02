package com.piticlistudio.playednext.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.ui.game.search.GameSearchFragment
import com.piticlistudio.playednext.util.ext.setContentFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class FooActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentFragment(R.id.content, { GameSearchFragment() })
    }

    private fun initView() {
        bottom_navigation_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_backlog -> {
                    bottom_navigation_view.setBackgroundResource(R.drawable.bottom_navigation_main_backlog_selector)
                    true
                }
                else -> { false }
            }
        }
    }

    override fun supportFragmentInjector() = fragmentInjector
}