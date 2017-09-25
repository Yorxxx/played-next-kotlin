package com.piticlistudio.playednext.data.game.repository

import com.piticlistudio.playednext.data.game.model.GameModel
import io.reactivex.Single

/**
 * Interface defining methods for the games. This is to be implemented by the remote layer, using this
 * interface as a way of communicating
 */
interface GameDatasourceRepository {

    fun load(id: Int): Single<GameModel>

    fun search(query: String): Single<List<GameModel>>
}