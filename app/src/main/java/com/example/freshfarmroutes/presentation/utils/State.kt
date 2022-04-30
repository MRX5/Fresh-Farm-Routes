package com.example.freshfarmroutes.presentation.utils

sealed class State<out R>{
    data class Success<out T>(val data:T):State<T>()
    data class Error(val errorType: ErrorType):State<Nothing>()
    object Loading:State<Nothing>()
    object Idle:State<Nothing>()
}

sealed class ErrorType{
    data class NoInternetConnection(val msg:String="لا يوجد اتصال بالأنترنت"):ErrorType()
    data class NoData(val msg:String="لا توجد بيانات"):ErrorType()
    data class UnknownException(val exception:String):ErrorType()
}