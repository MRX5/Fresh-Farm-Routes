package com.example.freshfarmroutes.presentation.home.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.freshfarmroutes.R
import com.example.freshfarmroutes.databinding.HyperLayoutBinding
import com.example.freshfarmroutes.domain.model.Hyper
import com.example.freshfarmroutes.presentation.utils.OnHyperClickListener

class HyperAdapter(private val listener: OnHyperClickListener) :
    RecyclerView.Adapter<HyperAdapter.HyperViewHolder>() {

    private var hyperList = mutableListOf<Hyper>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HyperViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<HyperLayoutBinding>(
            inflater,
            R.layout.hyper_layout,
            parent,
            false
        )
        return HyperViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HyperViewHolder, position: Int) {
        holder.bind(hyperList[position])
    }

    override fun getItemCount() = hyperList.size

    fun setData(hyperList: List<Hyper>) {
        this.hyperList = hyperList as MutableList<Hyper>
        notifyDataSetChanged()
    }

    inner class HyperViewHolder(private val binding: HyperLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hyper: Hyper) = with(binding) {
            this.hyper = hyper
            binding.root.setOnClickListener {
                listener.onHyperClick(hyper)
            }
        }
    }

}