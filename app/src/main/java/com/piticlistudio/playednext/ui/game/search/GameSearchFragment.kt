package com.piticlistudio.playednext.ui.game.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.jakewharton.rxbinding2.widget.RxTextView
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.ui.gamerelation.detail.GameRelationDetailActivity
import com.piticlistudio.playednext.util.NoNetworkAvailableException
import com.piticlistudio.playednext.util.SpacesItemDecoration
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.game_search_fragment.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.error
import org.jetbrains.anko.support.v4.startActivity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GameSearchFragment : Fragment(), AnkoLogger {

    @Inject lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var adapter: GameSearchPagedListAdapter
    private val viewModel by lazy {
        ViewModelProviders.of(this, mViewModelFactory).get(GameSearchViewModel::class.java)
    }
    private val searchSubject: PublishSubject<String> = PublishSubject.create()
    private var errorSnackBar: Snackbar? = null

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
        initSearchField()
        initAdapter()
        viewModel.getLoading().observe(this, Observer {
            if (it == true) searchLoading.show() else searchLoading.hide()
        })
        viewModel.getError().observe(this, Observer { handleError(it) })
    }

    private fun initAdapter() {
        recyclerview.adapter = adapter
        recyclerview.addItemDecoration(SpacesItemDecoration(8, 3))
        recyclerview.layoutManager = GridLayoutManager(activity, 3)
        adapter.onClickListener = { startActivity<GameRelationDetailActivity>("id" to it.id) }
    }

    private fun searchForResults(queryFilter: String) {
        viewModel.setQueryFilter(queryFilter)
        viewModel.searchResults.observe(this, Observer(adapter::setList))
    }

    private fun initSearchField() {
        searchClear.setOnClickListener { searchInputView.text.clear() }
        searchInputView.setOnEditorActionListener { textView, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    searchForResults(textView.text.toString()).run {
                        true
                    }
                }
                else -> false
            }
        }

        appBarView.addOnOffsetChangedListener { _, verticalOffset ->
            searchContainerView.translationY = verticalOffset.toFloat()
        }
        RxTextView.textChanges(searchInputView).subscribe { searchSubject.onNext(it.toString()) }

        searchSubject.debounce(350, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { searchForResults(it) }
    }

    private fun handleError(error: Exception?) {
        if (error == null) {
            errorSnackBar?.dismiss()
            searchInputView.isEnabled = true
        }

        error { "Received error ${error}" }
        error?.let {
            when (it) {
                is NoNetworkAvailableException -> {
                    errorSnackBar = longSnackbar(this.view!!, R.string.gamesearch_nonetwork_available)
                    searchInputView.isEnabled = false
                }
                else -> errorSnackBar = snackbar(this.view!!, it.localizedMessage, getString(R.string.gamesearch_retry)) { searchForResults(searchInputView.text.toString()) }
            }
        }
    }
}