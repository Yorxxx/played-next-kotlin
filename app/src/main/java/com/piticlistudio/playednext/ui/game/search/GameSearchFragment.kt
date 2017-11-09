package com.piticlistudio.playednext.ui.game.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.piticlistudio.playednext.R
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.game_search_fragment.*
import javax.inject.Inject

class GameSearchFragment: Fragment() {

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
        val viewmodel = ViewModelProviders.of(this, mViewModelFactory).get(GameSearchViewModel::class.java)

        val adapter = GameSearchPagedListAdapter()
        recyclerview.adapter = adapter
        viewmodel.searchResults.observe(this, Observer(adapter::setList))

        if (savedInstanceState == null)
            viewmodel.setQueryFilter("mario")
    }
}