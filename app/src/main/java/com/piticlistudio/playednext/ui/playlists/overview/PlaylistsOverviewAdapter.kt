package com.piticlistudio.playednext.ui.playlists.overview

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.piticlistudio.playednext.BR
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.ui.playlists.overview.model.PlaylistsOverviewModel
import javax.inject.Inject


class PlaylistsOverviewAdapter @Inject constructor() : ListAdapter<PlaylistsOverviewModel, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.playlist_overview_item, parent, false)
        return PlaylistOverviewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = (holder as PlaylistOverviewHolder).bindTo(getItem(position))

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<PlaylistsOverviewModel>() {

            override fun areItemsTheSame(oldItem: PlaylistsOverviewModel, newItem: PlaylistsOverviewModel): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: PlaylistsOverviewModel, newItem: PlaylistsOverviewModel): Boolean = oldItem == newItem
        }
    }

    private class PlaylistOverviewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: PlaylistsOverviewModel) {
            binding.setVariable(BR.model, item)
            binding.executePendingBindings()
        }
    }
}