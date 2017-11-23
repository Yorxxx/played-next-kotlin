package com.piticlistudio.playednext.ui.gamerelation.detail

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.piticlistudio.playednext.BR
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.domain.model.Game
import kotlinx.android.synthetic.main.game_detail_description_item_row.view.*
import javax.inject.Inject

class GameRelationDetailAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_TYPE_INFO = 100
        const val ITEM_TYPE_DESCRIPTION = 101
    }

    var game: Game? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is GameInfoHolder -> holder.bindTo(game)
            is GameDescriptionHolder -> holder.bindTo(game!!.summary)
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> return ITEM_TYPE_INFO
            1 -> return ITEM_TYPE_DESCRIPTION
            else -> return 10
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        when (viewType) {
            ITEM_TYPE_INFO -> {
                val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.game_detail_info_item_row, parent, false)
                return GameInfoHolder(binding)
            }
            ITEM_TYPE_DESCRIPTION -> {
                return GameDescriptionHolder(parent)
            }
        }
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private class GameInfoHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: Game?) {
            binding.setVariable(BR.game, item)
            binding.executePendingBindings()
        }
    }

    private class GameDescriptionHolder(parent: ViewGroup?) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent!!.context).inflate(R.layout.game_detail_description_item_row, parent, false)
    ) {
        fun bindTo(description: String?) {

            description?.let {
                val spannableString = SpannableString(it)
                var position = 0
                var i = 0
                val ei = it.length
                while (i < ei) {
                    val c = it.get(i)
                    if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9') {
                        position = i
                        break
                    }
                    i++
                }
                spannableString.setSpan(RelativeSizeSpan(3.0f), position, position + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                itemView.detail_description.setText(spannableString, TextView.BufferType.SPANNABLE)
            }
        }
    }
}
