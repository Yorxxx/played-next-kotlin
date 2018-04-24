package com.piticlistudio.playednext.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.ui.playlists.overview.PlaylistsOverviewFragment
import com.piticlistudio.playednext.util.ext.setContentFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class FooActivity : AppCompatActivity(), HasSupportFragmentInjector, FragmentManager.OnBackStackChangedListener {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        title = null

        supportFragmentManager.addOnBackStackChangedListener(this)
        setContentFragment(R.id.content, { PlaylistsOverviewFragment() })
    }

    override fun supportFragmentInjector() = fragmentInjector

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when (supportFragmentManager.backStackEntryCount) {
            0 -> super.onBackPressed()
            else -> supportFragmentManager.popBackStack()
        }
    }

    override fun onBackStackChanged() {
        supportActionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 1)
    }

    fun showToolbar(show: Boolean) {
        supportActionBar?.let {
            if (show) it.show()
            else it.hide()
        }
    }
}