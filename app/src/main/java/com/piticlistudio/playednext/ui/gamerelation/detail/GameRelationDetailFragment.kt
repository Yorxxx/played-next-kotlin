package com.piticlistudio.playednext.ui.gamerelation.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.databinding.GamerelationDetailBinding
import com.piticlistudio.playednext.util.ext.getScreenHeight
import com.squareup.picasso.Picasso
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.gamerelation_detail.*
import org.jetbrains.anko.AnkoLogger
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class GameRelationDetailFragment : Fragment(), AnkoLogger {

    companion object {
        private const val ARG_GAMEID = "game_id"

        fun newInstance(id: Int): GameRelationDetailFragment {
            val args = Bundle()
            args.putSerializable(ARG_GAMEID, id)
            val fragment = GameRelationDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val args by lazy {
        arguments?.let {
            GameRelationActivityArgs(it.getInt(ARG_GAMEID))
        }
    }

    @Inject lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var adapter: GameRelationDetailAdapter
    @Inject lateinit var platformadapter: GamePlatformsAdapter

    private var isAppBarCollapsed = false
    private val doubleClickSubject = PublishSubject.create<View>()
    private var binding: GamerelationDetailBinding? = null

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = layoutInflater.inflate(R.layout.gamerelation_detail, container, false)
        binding = DataBindingUtil.bind(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()

        val viewmodel = ViewModelProviders.of(this, mViewModelFactory).get(GameRelationDetailViewModel::class.java)

        viewmodel.getCurrentState().observe(this, Observer { it?.let { this.render(it) }})
        if (savedInstanceState == null && args != null)
            viewmodel.loadRelationForGame(args!!.gameId)
    }

    private fun initView() {

        val parent: AppCompatActivity = activity as AppCompatActivity
        parent.setSupportActionBar(toolbar)
        parent.supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setTitle(null)
        }

        detail_recyclerview.adapter = adapter
        val gridLayoutManager = GridLayoutManager(activity, 4)
        //gridLayoutManager.spanSizeLookup = 4
        platformsList.layoutManager = gridLayoutManager
        platformsList.adapter = platformadapter

        backdrop.layoutParams.height = (activity as AppCompatActivity).getScreenHeight()
        appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            backdrop.invalidate()
            isAppBarCollapsed = verticalOffset == -1 * appBarLayout.totalScrollRange
            if (isAppBarCollapsed) {
                collapsing_toolbar.title = toolbar.title
            } else {
                collapsing_toolbar.title = ""
            }
        }

        appbar.setOnClickListener { doubleClickSubject.onNext(it) }
        doubleClickSubject.buffer(300, TimeUnit.MILLISECONDS)
                .map { it.size == 2 }
                .filter { it }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { appbar.setExpanded(false) }

        toolbar.setOnClickListener { appbar.setExpanded(true) }
    }

    private fun render(viewState: ViewState) {
        when (viewState.isLoading) {
            true -> gamerelation_detail_loading.show()
            false -> gamerelation_detail_loading.hide()
        }
        viewState.game?.let {
            binding?.game = it
            adapter.game = it
            platformadapter.platforms = it.platforms ?: listOf()
        }
        adapter.relations = viewState.relations

        when (viewState.error) {
            null -> {
                Log.d("GameRelationDetailFragm", "No error")
            }
            else -> {
                Log.d("GameRelationDetailFragm", "Error found ${viewState.error}")
            }
        }
        viewState.showImage?.let {
            if (!isAppBarCollapsed)
                Picasso.with(context)
                        .load(it)
                        .into(backdrop)
        }
    }
}