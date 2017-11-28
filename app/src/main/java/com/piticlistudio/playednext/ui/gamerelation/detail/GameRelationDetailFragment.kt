package com.piticlistudio.playednext.ui.gamerelation.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.databinding.GamerelationDetailBinding
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.util.ext.getScreenHeight
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.gamerelation_detail.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GameRelationDetailFragment : Fragment() {

    @Inject lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var adapter: GameRelationDetailAdapter

    private var isAppBarCollapsed = false
    private val doubleClickSubject = PublishSubject.create<View>()
    private lateinit var binding: GamerelationDetailBinding

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = DataBindingUtil.inflate<GamerelationDetailBinding>(inflater, R.layout.gamerelation_detail, container, false).also {
            binding = it
        }.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        val viewmodel = ViewModelProviders.of(this, mViewModelFactory).get(GameRelationDetailViewModel::class.java)

        viewmodel.getLoading().observe(this, Observer {
            it?.let {
                if (it) gamerelation_detail_loading.show() else gamerelation_detail_loading.hide()
            }
        })
        viewmodel.getGame().observe(this, Observer {
            binding.game = it
            adapter.game = it
        })
        viewmodel.getScreenshot().observe(this, Observer {
            if (!isAppBarCollapsed)
                Glide.with(context).load(it).into(backdrop)
        })
        viewmodel.getRelations().observe(this, Observer {
            it?.let { adapter.relations = it }
            it?.forEach {
                Log.d("GameRelationDetailFragm", "Retrieved relation with status ${it.currentStatus.name} for platform ${it.platform?.name}")
            }
//            it?.forEachIndexed({index, gameRelation ->
//                if (gameRelation.currentStatus !== GameRelationStatus.values()[index]) {
//                    gameRelation.currentStatus = GameRelationStatus.values()[index];
//                    viewmodel.saveRelation(gameRelation)
//                }
//            })
        })
        viewmodel.getError().observe(this, Observer {
            when (it) {
                null -> {
                    Log.d("GameRelationDetailFragm", "No error")
                }
                else -> {
                    Log.d("GameRelationDetailFragm", "Error found ${it}")
                }
            }
        })
        if (savedInstanceState == null)
            viewmodel.loadRelationForGame(124)
    }

    private fun initView() {

        val parent: AppCompatActivity = activity as AppCompatActivity
        parent.setSupportActionBar(toolbar)
        parent.supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setTitle(null)
        }

        detail_recyclerview.adapter = adapter

        backdrop.layoutParams.height = activity.getScreenHeight()
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
}