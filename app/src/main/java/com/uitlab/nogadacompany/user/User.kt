package com.uitlab.nogadacompany.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val id:Int, val email:String, var name:String, val imgsrc:String):Parcelable
