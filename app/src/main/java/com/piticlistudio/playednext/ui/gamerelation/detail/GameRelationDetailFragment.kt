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
import com.piticlistudio.playednext.domain.model.GameRelation
import com.squareup.picasso.Picasso
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.gamerelation_detail.*
import javax.inject.Inject

class GameRelationDetailFragment : Fragment() {

    @Inject lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var picasso: Picasso

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View? = inflater?.inflate(R.layout.gamerelation_detail, container, false)
        return view!!
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewmodel = ViewModelProviders.of(this, mViewModelFactory).get(GameRelationDetailViewModel::class.java)

        viewmodel.getLoading().observe(this, Observer {
            when (it) {
                false -> {
                    gamerelation_detail_loading.hide()
                }
                else -> {
                    gamerelation_detail_loading.show()
                }
            }
        })
        viewmodel.getData().observe(this, Observer { showData(it) })
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
            viewmodel.loadRelation()
    }

    private fun showData(data: GameRelation?) {
        when (data) {
            null -> {
                detail_content.visibility = View.INVISIBLE
            }
            else -> {
                detail_content.visibility = View.VISIBLE
                data.game?.apply {
                    backdropTitle.text = name
                    images?.forEach {
                        Log.d("GameRelationDetailFragm", "showData (line 74): ${it}")
                    }
                }
            }
        }
    }
}