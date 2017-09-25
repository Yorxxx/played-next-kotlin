package com.piticlistudio.playednext.features.detail

import com.piticlistudio.playednext.data.model.Pokemon
import com.piticlistudio.playednext.data.model.Statistic
import com.piticlistudio.playednext.features.base.MvpView

interface DetailMvpView : MvpView {

    fun showPokemon(pokemon: Pokemon)

    fun showStat(statistic: Statistic)

    fun showProgress(show: Boolean)

    fun showError(error: Throwable)

}