package com.piticlistudio.playednext.ui.gamerelation.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.piticlistudio.playednext.R
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class GameRelationDetailFragment : Fragment() {

    @Inject lateinit var mViewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View? = inflater?.inflate(R.layout.game_search_fragment, container, false)
        return view!!
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewmodel = ViewModelProviders.of(this, mViewModelFactory).get(GameRelationDetailViewModel::class.java)

        viewmodel.getLoading().observe(this, Observer {
            Log.d("FooActivity", "Is loading ${it}")
        })
        viewmodel.getData().observe(this, Observer {
            when (it) {
                null -> {
                    Log.d("FooActivity", "No relation available")
                }
                else -> {
                    Log.d("FooActivity", "Retrieved relation ${it}")
                }
            }
        })
        viewmodel.getError().observe(this, Observer {
            when (it) {
                null -> {
                    Log.d("FooActivity", "No error")
                }
                else -> {
                    Log.d("FooActivity", "Error found ${it}")
                }
            }
        })
        if (savedInstanceState == null)
            viewmodel.loadRelation()
    }
}