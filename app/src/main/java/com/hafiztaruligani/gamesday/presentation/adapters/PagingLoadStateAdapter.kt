package com.hafiztaruligani.gamesday.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hafiztaruligani.gamesday.R
import com.hafiztaruligani.gamesday.databinding.ItemLoadStateBinding

class PagingLoadStateAdapter(
    private val retry: ()->Unit
): LoadStateAdapter<PagingLoadStateAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: ItemLoadStateBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        val isLoading = loadState is LoadState.Loading
        holder.binding.apply {
            loading.isVisible = isLoading

            val error = loadState is LoadState.Error
            message.isVisible = error
            btn.isVisible = error
            if(error) setErrorMessage(this)

        }

    }

    private fun setErrorMessage(binding: ItemLoadStateBinding){
        binding.apply {
            message.text = binding.root.context.resources.getString(R.string.network_is_unstable)
            btn.setOnClickListener {
                retry.invoke()
            }
        }
    }
}