package com.piticlistudio.playednext.ui.game.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.util.SpacesItemDecoration
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.game_search_fragment.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GameSearchFragment : Fragment() {

    @Inject lateinit var mViewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProviders.of(this, mViewModelFactory).get(GameSearchViewModel::class.java)
    }
    private val searchSubject: PublishSubject<String> = PublishSubject.create()
    private var adapter: GameSearchPagedListAdapter? = null

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
    }

    private fun initAdapter() {
        adapter = GameSearchPagedListAdapter()
        recyclerview.adapter = adapter
        recyclerview.addItemDecoration(SpacesItemDecoration(8, 3))
        recyclerview.layoutManager = GridLayoutManager(activity, 3)
    }

    private fun searchForResults(queryFilter: String) {
        viewModel.setQueryFilter(queryFilter)
        adapter?.let { viewModel.searchResults.observe(this, Observer(adapter!!::setList)) }
    }

    private fun initSearchField() {
        searchClear.setOnClickListener { searchInputView.text.clear() }

        appBarView.addOnOffsetChangedListener { _, verticalOffset ->
            searchContainerView.translationY = verticalOffset.toFloat()
        }
        RxTextView.textChanges(searchInputView).subscribe { searchSubject.onNext(it.toString()) }

        searchSubject.debounce(350, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { searchForResults(it) }
    }
}