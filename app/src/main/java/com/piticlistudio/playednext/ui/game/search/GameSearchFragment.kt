package com.piticlistudio.playednext.ui.game.search

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.features.game.search.GameSearchContract
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.game_search_fragment.*
import javax.inject.Inject

class GameSearchFragment : Fragment(), GameSearchContract.View {

    @Inject
    lateinit var presenter: GameSearchContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View? = inflater?.inflate(R.layout.game_search_fragment, container, false)
        return view!!
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidInjection.inject(this)
        presenter.attachView(this)
        this.search("mario")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showLoading() {
        text_something.text = "showLoading"
    }

    override fun hideLoading() {
        text_something.text = "hideLoading"
    }

    override fun showData(data: List<Game>) {
        text_something.text = "showData"
    }

    override fun hideData() {
        text_something.text = "hideData"
    }

    override fun showError(error: Throwable) {
        text_something.text = "showError"
    }

    override fun hideError() {
        text_something.text = "hideError"
    }

    override fun search(query: String) {
        presenter.search(query)
    }
}