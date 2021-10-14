package com.uitlab.nogadacompany.user

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserAttendance (
    @SerializedName("wid")
    var uid: String,

    @SerializedName("atDate")
    var date: Date
)