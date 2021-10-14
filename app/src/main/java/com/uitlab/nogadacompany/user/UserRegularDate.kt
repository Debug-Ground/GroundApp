package com.uitlab.nogadacompany.user

import com.google.gson.annotations.SerializedName

data class UserRegularDate (

    @SerializedName("message")
    var message: String,
    @SerializedName("result")
    var result: MutableList<RegularDate>
    )

data class RegularDate(
    @SerializedName("wRegular")
    var wRegular: String,

    @SerializedName("wDate")
    var wDate: String
)

