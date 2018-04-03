package com.piticlistudio.playednext.ui.gamerelation.detail

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.piticlistudio.playednext.BR
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.domain.model.Platform
import javax.inject.Inject

class GamePlatformsAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var platforms: List<Platform> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) = (holder as PlatformHolder).bindTo(platforms.get(position))

    override fun getItemCount(): Int = platforms.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.platform_label_row, parent, false)
        return PlatformHolder(binding)
    }

    private class PlatformHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: Platform?) {
            binding.setVariable(BR.platform, item)
            binding.executePendingBindings()
        }
    }
}