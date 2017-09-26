package com.piticlistudio.playednext.features

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.features.game.search.GameSearchContract
import dagger.android.AndroidInjection
import javax.inject.Inject

class FooActivity : AppCompatActivity(), GameSearchContract.View {

    @Inject
    lateinit var presenter: GameSearchContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_search_fragment)
        AndroidInjection.inject(this)
        presenter.attachView(this)
        this.search("mario")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showLoading() {
        Log.d("FOO", "Show loading called")
    }

    override fun hideLoading() {
        Log.d("FOO", "hideLoading called")
    }

    override fun showData(data: List<Game>) {
        Log.d("FOO", "showData called")
    }

    override fun hideData() {
        Log.d("FOO", "hideData called")
    }

    override fun showError(error: Throwable) {
        Log.d("FOO", "showError called")
    }

    override fun hideError() {
        Log.d("FOO", "hideError called")
    }

    override fun search(query: String) {
        presenter.search(query)
    }
}