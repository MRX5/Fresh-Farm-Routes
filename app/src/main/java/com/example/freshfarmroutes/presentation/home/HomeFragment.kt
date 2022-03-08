package com.example.freshfarmroutes.presentation.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshfarmroutes.R
import com.example.freshfarmroutes.databinding.HomeFragmentBinding
import com.example.freshfarmroutes.domain.model.Hyper
import com.example.freshfarmroutes.presentation.home.adapter.HyperAdapter
import com.example.freshfarmroutes.presentation.utils.LinearSpacingItemDecoration
import com.example.freshfarmroutes.presentation.utils.OnHyperClickListener
import com.example.freshfarmroutes.presentation.utils.State
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment(),OnHyperClickListener {
    private lateinit var binding: HomeFragmentBinding
    private val viewModel: HomeViewModel by viewModels()
    private val hyperAdapter by lazy { HyperAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        getHyper()
    }

    private fun setupRecycler() {
        binding.homeRecyclerview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(LinearSpacingItemDecoration())
            setHasFixedSize(true)
            adapter = hyperAdapter
        }
    }

    private fun getHyper() {
        lifecycleScope.launchWhenCreated {
            viewModel.hyperStateFlow.collect {
                when (it) {
                    is State.Loading -> {
                        binding.progressBar.visibility = VISIBLE
                    }
                    is State.Success -> {
                        binding.progressBar.visibility = GONE
                        hyperAdapter.setData(it.data)
                    }
                    is State.Error -> {
                        binding.progressBar.visibility = GONE
                        showSnackBar(it.exception)
                    }
                }
            }
        }
    }

    private fun showSnackBar(Msg: String) {
        Snackbar.make(binding.root, Msg, Snackbar.LENGTH_INDEFINITE).apply {
            setAction("إعادة المحاولة") {
                viewModel.getHyper()
                dismiss()
            }
            setActionTextColor(resources.getColor(R.color.yellow,null))
            show()
        }
    }

    override fun onHyperClick(hyper: Hyper) {
        val directions=HomeFragmentDirections.actionNavHomeToNavBranches(hyper)
        findNavController().navigate(directions)
    }
}