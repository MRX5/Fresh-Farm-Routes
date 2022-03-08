package com.example.freshfarmroutes.presentation.branches

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freshfarmroutes.R
import com.example.freshfarmroutes.databinding.BranchesFragmentBinding
import com.example.freshfarmroutes.domain.model.Hyper
import com.example.freshfarmroutes.presentation.MainActivity
import com.example.freshfarmroutes.presentation.branches.adapter.BranchesAdapter
import com.example.freshfarmroutes.presentation.utils.OnButtonClickListener
import com.example.freshfarmroutes.presentation.utils.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class BranchesFragment : Fragment(),OnButtonClickListener{
    private lateinit var binding: BranchesFragmentBinding
    private val viewModel:BranchesViewModel by viewModels()
    private val branchesAdapter by lazy { BranchesAdapter(this) }
    private lateinit var hyper: Hyper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.branches_fragment,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle=arguments


        if(bundle!=null){
            val args=BranchesFragmentArgs.fromBundle(bundle)
            hyper=args.hyper
        }

        (activity as MainActivity).supportActionBar?.title=hyper.name

        setupSearchView()
        setRecyclerView()
        getBranches()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                branchesAdapter.filter.filter(newText)
                return false
            }
        })
    }


    private fun setRecyclerView() {
        binding.branchesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = branchesAdapter
        }
    }

    private fun getBranches(){
        viewModel.getBranches(hyper.id)
        lifecycleScope.launchWhenCreated {
            viewModel.branchesStateFlow.collect {
                when(it){
                    is State.Loading->{
                        binding.branchesProgressBar.visibility=VISIBLE
                    }
                    is State.Success->{
                        binding.branchesProgressBar.visibility= GONE
                        branchesAdapter.setData(it.data)
                    }
                    is State.Error->{
                        binding.branchesProgressBar.visibility= GONE
                        binding.noInternetLayout.visibility= VISIBLE
                    }
                }
            }
        }
    }

    override fun onShareBtnClick(locationUrl:String) {
        val sendIntent=Intent().apply {
            action=Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, locationUrl)
            type = "text/plain"
        }
        startActivity(sendIntent)
    }

    override fun onGoToLocationClick(locationUrl:String) {
        val uri = Uri.parse(locationUrl)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

}