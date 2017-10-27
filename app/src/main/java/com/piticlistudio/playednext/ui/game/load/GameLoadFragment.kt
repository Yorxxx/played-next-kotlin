package com.piticlistudio.playednext.ui.game.load

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.features.game.load.GameLoadContract
import dagger.android.AndroidInjection
import javax.inject.Inject

class GameLoadFragment : Fragment(), GameLoadContract.View {

    @Inject
    lateinit var presenter: GameLoadContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View? = inflater?.inflate(R.layout.game_search_fragment, container, false)
        return view!!
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidInjection.inject(this)
        presenter.attachView(this)
        this.load(1004)
        //this.search("mario")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showLoading() {
        Log.i("GameLoadFragment", "showLoadingCalled")
    }

    override fun hideLoading() {
        Log.i("GameLoadFragment", "hideLoading")
    }

    override fun showData(data: Game) {
        Log.i("GameLoadFragment", "showData {$data}")
    }

    override fun hideData() {
        Log.i("GameLoadFragment", "hideData")
    }

    override fun showError(error: Throwable) {
        Log.i("GameLoadFragment", "showError {$error}")
    }

    override fun hideError() {
        Log.i("GameLoadFragment", "hideError")
    }

    override fun load(id: Int) {
        presenter.load(id)
    }
}