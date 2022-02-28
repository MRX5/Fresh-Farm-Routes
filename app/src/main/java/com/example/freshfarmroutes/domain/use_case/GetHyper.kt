package com.example.freshfarmroutes.domain.use_case

import com.example.freshfarmroutes.domain.model.Hyper
import com.example.freshfarmroutes.domain.repository.HyperRepository
import com.example.freshfarmroutes.presentation.utils.State
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHyper @Inject constructor(private val repository: HyperRepository) {

    suspend operator fun invoke(): Flow<State<List<Hyper>>> {
        return repository.getHyper()
    }
}