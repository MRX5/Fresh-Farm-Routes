package com.example.freshfarmroutes.presentation.branches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshfarmroutes.domain.model.Branch
import com.example.freshfarmroutes.domain.use_case.GetBranches
import com.example.freshfarmroutes.presentation.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BranchesViewModel @Inject constructor(
    private val getBranchesUseCase: GetBranches
) : ViewModel() {

    private var _branchesStateFlow = MutableStateFlow<State<List<Branch>>>(State.Idle)
    val branchesStateFlow: MutableStateFlow<State<List<Branch>>> get() = _branchesStateFlow

    init {
        getBranches()
    }

    fun getBranches() {
        viewModelScope.launch {
            getBranchesUseCase.invoke().onEach {
                _branchesStateFlow.value=it
            }.launchIn(viewModelScope)
        }
    }
}