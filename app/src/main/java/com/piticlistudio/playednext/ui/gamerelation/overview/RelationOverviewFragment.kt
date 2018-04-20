package com.piticlistudio.playednext.ui.gamerelation.overview

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.relation.SaveGameRelationUseCase
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.ui.FooActivity
import com.piticlistudio.playednext.util.SpacesItemDecoration
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.gamerelation_overview.*
import rx.subjects.PublishSubject
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RelationOverviewFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var adapter: RelationOverviewAdapter
    private val viewModel by lazy {
        ViewModelProviders.of(this, mViewModelFactory).get(RelationOverviewViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.gamerelation_overview, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()

        viewModel.getOverview().observe(this, Observer(adapter::submitList))

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
        gamerelation_overview_recyclerview.layoutManager = LinearLayoutManager(activity)
        gamerelation_overview_recyclerview.addItemDecoration(SpacesItemDecoration(32, 1))
    }
}