package com.example.freshfarmroutes.data.repository

import android.util.Log
import com.example.freshfarmroutes.data.utils.NetworkHelper
import com.example.freshfarmroutes.domain.model.Branch
import com.example.freshfarmroutes.domain.model.Hyper
import com.example.freshfarmroutes.domain.repository.HyperRepository
import com.example.freshfarmroutes.presentation.utils.ErrorType
import com.example.freshfarmroutes.presentation.utils.State
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HyperRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val networkHelper: NetworkHelper
) : HyperRepository {

    override suspend fun getBranches(hyperId:String) = flow {
        emit(State.Loading)
        val response = fireStore.
        collection("Hyper")
            .document(hyperId)
            .collection("Branches")
            .get().await()
        if (response.isEmpty) {
            if (!networkHelper.isNetworkConnected()) {
                emit(State.Error(ErrorType.NoInternetConnection()))
            } else {
                emit(State.Error(ErrorType.NoData()))
            }
        } else {
            val branchesList = response.documents.mapNotNull {
                it.toObject(Branch::class.java)
            }
            emit(State.Success(branchesList))
        }
    }.catch { error ->
        error.message?.let {
            emit(State.Error(ErrorType.UnknownException(it)))
        }
    }

    override suspend fun getHyper() = flow {
        emit(State.Loading)
        val response = fireStore.collection("Hyper")
            .get().await()
        if (response.isEmpty) {
            if (!networkHelper.isNetworkConnected()) {
                emit(State.Error(ErrorType.NoInternetConnection()))
            } else {
                emit(State.Error(ErrorType.NoData()))
            }
        } else {
            val hyperList = response.documents.mapNotNull {
                it.toObject(Hyper::class.java)
            }
            emit(State.Success(hyperList))
        }
    }.catch { error ->
        error.message?.let {
            emit(State.Error(ErrorType.UnknownException(it)))
        }
    }

}