package com.piticlistudio.playednext.ui.gamerelation.detail

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.piticlistudio.playednext.domain.model.Game
import javax.inject.Inject

class GameRelationDetailAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_TYPE_INFO = 100
    }

    var game: Game? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is GameInfoHolder -> holder.bindTo(game)
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> return ITEM_TYPE_INFO
            else -> return 10
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_TYPE_INFO -> return GameInfoHolder(parent!!)
        }
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
