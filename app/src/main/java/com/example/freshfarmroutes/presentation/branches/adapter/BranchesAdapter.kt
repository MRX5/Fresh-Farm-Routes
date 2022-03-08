package com.example.freshfarmroutes.presentation.branches.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import com.example.freshfarmroutes.R
import com.example.freshfarmroutes.databinding.BranchLayoutBinding
import com.example.freshfarmroutes.databinding.HyperLayoutBinding
import com.example.freshfarmroutes.domain.model.Branch
import com.example.freshfarmroutes.presentation.utils.OnButtonClickListener

class BranchesAdapter(private val listener:OnButtonClickListener) : RecyclerView.Adapter<BranchesAdapter.BranchesViewHolder>(),Filterable{

    var branchesList: MutableList<Branch> = mutableListOf()
    var branchesFilterList= mutableListOf<Branch>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BranchesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<BranchLayoutBinding>(
                inflater,
                R.layout.branch_layout,
                parent,
                false
            )
        return BranchesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BranchesViewHolder, position: Int) =
        holder.bind(branchesFilterList[position])

    override fun getItemCount() = branchesFilterList.size

    fun setData(branchesList: List<Branch>) {
        this.branchesList = branchesList as MutableList<Branch>
        branchesFilterList=branchesList
        notifyDataSetChanged()
    }

    inner class BranchesViewHolder(private val binding: BranchLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(branch: Branch) = with(binding) {
           this.branch=branch
            binding.goBtn.setOnClickListener {  listener.onGoToLocationClick(branch.location_url)}
            binding.shareBtn.setOnClickListener { listener.onShareBtnClick(branch.location_url) }
        }
    }

    override fun getFilter(): Filter=object :Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val charSearch = constraint.toString()
            if (charSearch.isEmpty()) {
                branchesFilterList = branchesList
            } else {
                val resultList = mutableListOf<Branch>()
                for (branch in branchesList) {
                    if (branch.name.contains(charSearch)) {
                        resultList.add(branch)
                    }
                }
                branchesFilterList = resultList
            }
            val filterResults = FilterResults()
            filterResults.values = branchesFilterList
            return filterResults
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            branchesFilterList= results?.values as MutableList<Branch>
            notifyDataSetChanged()
        }

    }
}