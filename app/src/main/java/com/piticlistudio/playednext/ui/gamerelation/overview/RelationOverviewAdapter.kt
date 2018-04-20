package com.piticlistudio.playednext.ui.gamerelation.overview

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.piticlistudio.playednext.BR
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.ui.gamerelation.overview.model.RelationOverviewModel
import javax.inject.Inject

class RelationOverviewAdapter @Inject constructor() : ListAdapter<RelationOverviewModel, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.gamerelation_overview_item, parent, false)
        return RelationOverviewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = (holder as RelationOverviewHolder).bindTo(getItem(position))

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<RelationOverviewModel>() {

            override fun areItemsTheSame(oldItem: RelationOverviewModel, newItem: RelationOverviewModel): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: RelationOverviewModel, newItem: RelationOverviewModel): Boolean = oldItem == newItem
        }
    }

    private class RelationOverviewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: RelationOverviewModel) {
            binding.setVariable(BR.relation, item)
            binding.executePendingBindings()
        }
    }
}