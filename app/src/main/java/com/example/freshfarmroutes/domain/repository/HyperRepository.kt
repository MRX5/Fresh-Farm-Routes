package com.example.freshfarmroutes.domain.repository

import com.example.freshfarmroutes.domain.model.Branch
import com.example.freshfarmroutes.domain.model.Hyper
import com.example.freshfarmroutes.presentation.utils.State
import kotlinx.coroutines.flow.Flow

interface HyperRepository {
    suspend fun  getBranches(hyperId:String):Flow<State<List<Branch>>>
    suspend fun  getHyper():Flow<State<List<Hyper>>>
}