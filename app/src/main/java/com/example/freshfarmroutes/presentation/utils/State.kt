package com.example.freshfarmroutes.presentation.utils

sealed class State<out R>{
    data class Success<out T>(val data:T):State<T>()
    data class Error(val exception:String):State<Nothing>()
    object Loading:State<Nothing>()
    object Idle:State<Nothing>()

}
