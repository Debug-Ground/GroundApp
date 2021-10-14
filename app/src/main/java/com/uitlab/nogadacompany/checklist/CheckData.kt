package com.uitlab.nogadacompany.checklist

import com.google.gson.annotations.SerializedName

data class CheckData(
    @SerializedName("message")
    var message: String,
    @SerializedName("result")
    var result: MutableList<CheckDataApi>
   )

data class CheckDataApi (
    @SerializedName("cid")
    var cid: Int = 0,
    @SerializedName("cList")
    var cList:String
)