package com.ps.androidx.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ps.androidx.R
import com.ps.androidx.data.model.unsplash.Unsplash
import com.ps.androidx.databinding.LayoutNetworkStateBinding
import com.ps.androidx.databinding.LayoutPreviewBinding
import com.ps.androidx.network.NetworkState


/**
 * Created by Prokash Sarkar on 5/24/2019.
 * https://github.com/prokash-sarkar
 */

class UnsplashAdapter(private val context: Context, private val callback: Callback) :
    PagedListAdapter<Unsplash, RecyclerView.ViewHolder>(COMPARATOR) {

    private var networkState: NetworkState.State? = null

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Unsplash>() {
            override fun areItemsTheSame(oldItem: Unsplash, newItem: Unsplash): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Unsplash, newItem: Unsplash): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface Callback {
        fun onItemClick(unsplash: Unsplash)
        fun onClickRetry()
        fun onListUpdated(size: Int, networkState: NetworkState.State?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.layout_preview -> {
                val binding = DataBindingUtil
                    .inflate<LayoutPreviewBinding>(
                        LayoutInflater.from(context),
                        R.layout.layout_preview, parent, false
                    )
                UnsplashViewHolder(binding)
            }
            R.layout.layout_network_state -> {
                val binding = DataBindingUtil
                    .inflate<LayoutNetworkStateBinding>(
                        LayoutInflater.from(context),
                        R.layout.layout_network_state, parent, false
                    )
                NetworkStateViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.layout_preview -> {
                val viewHolder = holder as UnsplashViewHolder
                viewHolder.bindTo(position, viewHolder)
            }
            R.layout.layout_network_state -> {
                val viewHolder = holder as NetworkStateViewHolder
                viewHolder.bindTo(viewHolder)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.layout_network_state
        } else {
            R.layout.layout_preview
        }
    }

    override fun getItemCount(): Int {
        this.callback.onListUpdated(super.getItemCount(), this.networkState)
        return super.getItemCount()
    }

    /**
     * View Holder
     */

    inner class UnsplashViewHolder(private val binding: LayoutPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(position: Int, viewHolder: UnsplashViewHolder) {
            val unsplash = getItem(position)
            unsplash?.let {
                viewHolder.binding.unsplash = unsplash
                viewHolder.itemView.setOnClickListener { callback.onItemClick(unsplash) }
            }
        }
    }

    inner class NetworkStateViewHolder(private val binding: LayoutNetworkStateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(viewHolder: NetworkStateViewHolder) {
            viewHolder.toggleViews(isLoading = false, isVisible = false)
            viewHolder.checkNetworkState(networkState)
            viewHolder.binding.btnRetry.setOnClickListener { callback.onClickRetry() }
        }

        private fun toggleViews(isLoading: Boolean, isVisible: Boolean) {
            val progressVisibility = if (isLoading) View.VISIBLE else View.GONE
            binding.pbLoading.visibility = progressVisibility

            val viewVisibility = if (isVisible) View.VISIBLE else View.GONE
            binding.btnRetry.visibility = viewVisibility
            binding.tvMessage.visibility = viewVisibility
        }

        private fun checkNetworkState(networkState: NetworkState.State?) {
            when (networkState) {
                NetworkState.State.LOADING -> {
                    toggleViews(isLoading = true, isVisible = false)
                }
                NetworkState.State.SUCCESS -> {
                    toggleViews(isLoading = false, isVisible = false)
                }
                NetworkState.State.ERROR -> {
                    toggleViews(isLoading = false, isVisible = true)
                }
            }
        }

    }

    /**
     * Extras
     */

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.State.SUCCESS

    fun updateNetworkState(newNetworkState: NetworkState.State?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
        notifyDataSetChanged()
    }

}