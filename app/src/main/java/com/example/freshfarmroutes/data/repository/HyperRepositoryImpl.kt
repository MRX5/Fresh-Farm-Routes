package com.example.freshfarmroutes.data.repository

import android.util.Log
import com.example.freshfarmroutes.domain.model.Branch
import com.example.freshfarmroutes.domain.model.Hyper
import com.example.freshfarmroutes.domain.repository.HyperRepository
import com.example.freshfarmroutes.presentation.utils.State
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class HyperRepositoryImpl : HyperRepository {

    @ExperimentalCoroutinesApi
    override suspend fun getBranches()= callbackFlow {
        val db = Firebase.firestore
        trySend(State.Loading)
        db.collection("Branch")
            .get()
            .addOnSuccessListener { result ->
                val branchesList = mutableListOf<Branch>()
                for (branch in result) {
                    branchesList.add(branch.toObject(Branch::class.java))
                }
                trySend(State.Success(branchesList))
            }.addOnFailureListener {
                trySend(State.Error(it.message.toString()))
            }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getHyper() = callbackFlow {
        val db = Firebase.firestore
        trySend(State.Loading)
        db.collection("Hyper")
            .get()
            .addOnSuccessListener { result ->
                val hyperList = mutableListOf<Hyper>()
                result.forEach { hyper ->
                    hyperList.add(hyper.toObject(Hyper::class.java))
                }

                trySend(State.Success(hyperList))
            }
            .addOnFailureListener {
                trySend(State.Error(it.message.toString()))
            }

        awaitClose {
            cancel()
        }
    }

}