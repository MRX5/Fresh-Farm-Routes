package com.example.freshfarmroutes.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshfarmroutes.domain.model.Branch
import com.example.freshfarmroutes.domain.model.Hyper
import com.example.freshfarmroutes.domain.use_case.GetHyper
import com.example.freshfarmroutes.presentation.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHyperUseCase: GetHyper
) : ViewModel() {

    private var _hyperStateFlow= MutableStateFlow<State<List<Hyper>>>(State.Idle)
    val hyperStateFlow: MutableStateFlow<State<List<Hyper>>> get()=_hyperStateFlow

    init{
        getHyper()
    }

    fun getHyper(){
        viewModelScope.launch {
            getHyperUseCase.invoke().onEach {
                _hyperStateFlow.value=it
            }.launchIn(viewModelScope)
        }
    }
}