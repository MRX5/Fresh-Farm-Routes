package com.example.freshfarmroutes.presentation.branches

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.freshfarmroutes.presentation.utils.ErrorType
import com.example.freshfarmroutes.presentation.utils.OnButtonClickListener
import com.example.freshfarmroutes.presentation.utils.OnRetryButtonClickListener
import com.example.freshfarmroutes.presentation.utils.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class BranchesFragment : Fragment(),OnButtonClickListener,OnRetryButtonClickListener{
    private lateinit var binding: BranchesFragmentBinding
    private val viewModel:BranchesViewModel by viewModels()
    private val branchesAdapter by lazy { BranchesAdapter(this) }
    private lateinit var hyper: Hyper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.branches_fragment,container,false)
        binding.errorLayout.handler=this
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
                        binding.errorLayout.root.visibility=GONE
                        binding.branchesProgressBar.visibility=VISIBLE
                    }
                    is State.Success->{
                        binding.branchesProgressBar.visibility= GONE
                        branchesAdapter.setData(it.data)
                    }
                    is State.Error->{
                        binding.branchesProgressBar.visibility= GONE
                        showError(it.errorType)
                    }
                }
            }
        }
    }

    private fun showError(errorType: ErrorType) {
        when(errorType){
            is ErrorType.NoInternetConnection->{
                binding.errorLayout.errorImage.setImageResource(R.drawable.icon_no_internet)
                binding.errorLayout.errorText.text=errorType.msg
                binding.errorLayout.root.visibility= VISIBLE
            }
            is ErrorType.NoData->{
                binding.errorLayout.errorImage.setImageResource(R.drawable.icon_empty)
                binding.errorLayout.errorText.text=errorType.msg
                binding.errorLayout.root.visibility= VISIBLE
            }
            is ErrorType.UnknownException->{
                binding.errorLayout.errorImage.setImageResource(R.drawable.icon_empty)
                binding.errorLayout.errorText.text="حدث خطأ"
                binding.errorLayout.root.visibility= VISIBLE
                Toast.makeText(context,errorType.exception,Toast.LENGTH_LONG).show()
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

    override fun onRetryBtnClick() {
        viewModel.getBranches(hyper.id)
    }

}