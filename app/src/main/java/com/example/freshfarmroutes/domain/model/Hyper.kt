package com.example.freshfarmroutes.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hyper(
     val id:String="",
     val name:String?="",
     val logo_url:String?=""
):Parcelable
