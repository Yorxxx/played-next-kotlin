package com.piticlistudio.playednext.ui.playlists.overview

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.databinding.PlaylistOverviewBinding
import com.piticlistudio.playednext.ui.FooActivity
import com.piticlistudio.playednext.util.SpacesItemDecoration
import com.squareup.picasso.Picasso
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.gamerelation_detail.*
import kotlinx.android.synthetic.main.gamerelation_overview.*
import javax.inject.Inject

class PlaylistsOverviewFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var adapter: PlaylistsOverviewAdapter
    private val viewModel by lazy {
        ViewModelProviders.of(this, mViewModelFactory).get(PlaylistsOverviewViewModel::class.java)
    }
    private var binding: PlaylistOverviewBinding? = null

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.playlist_overview, container, false)
        binding = DataBindingUtil.bind(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        viewModel.getViewState().observe(this, Observer { it?.let { this.render(it) }})

        if (savedInstanceState == null) {
            viewModel.start()
        }
    }

    private fun initView() {

        (activity as FooActivity).run {
            showToolbar(false)
        }
//        val parent: AppCompatActivity = activity as AppCompatActivity
//        parent.setSupportActionBar(toolbar)

        gamerelation_overview_recyclerview.adapter = adapter
        gamerelation_overview_recyclerview.layoutManager = GridLayoutManager(activity, 2)
        gamerelation_overview_recyclerview.addItemDecoration(SpacesItemDecoration(32, 1))
    }

    private fun render(viewState: ViewState) {
        binding?.model = viewState
        adapter.submitList(viewState.items)
//        when (viewState.isLoading) {
//            true -> gamerelation_detail_loading.show()
//            false -> gamerelation_detail_loading.hide()
//        }
//        viewState.game?.let {
//            binding?.game = it
//            adapter.game = it
//            platformadapter.platforms = it.platforms ?: listOf()
//        }
//        adapter.relations = viewState.relations
//
//        when (viewState.error) {
//            null -> {
//                Log.d("GameRelationDetailFragm", "No error")
//            }
//            else -> {
//                Log.d("GameRelationDetailFragm", "Error found ${viewState.error}")
//            }
//        }
//        viewState.showImage?.let {
//            if (!isAppBarCollapsed)
//                Picasso.with(context)
//                        .load(it)
//                        .into(backdrop)
//        }
    }
}