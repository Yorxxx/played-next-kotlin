package com.piticlistudio.playednext.features

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.piticlistudio.playednext.features.game.load.GameDetailViewModel
import com.piticlistudio.playednext.features.gamerelation.load.GameRelationDetailViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val relationDetailVM: GameRelationDetailViewModel,
                                           private val gameDetailVM: GameDetailViewModel): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameRelationDetailViewModel::class.java)) {
            return relationDetailVM as T
        }
        if (modelClass.isAssignableFrom(GameDetailViewModel::class.java)) {
            return gameDetailVM as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}