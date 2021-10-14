package com.uitlab.nogadacompany.noticedata

import com.google.gson.annotations.SerializedName

data class NoticeData(
    @SerializedName("message")
    var message: String,
    @SerializedName("result")
    var result: MutableList<Notice>
)

data class Notice (
    @SerializedName("anid")
    var nid: Int = 0,
    @SerializedName("antitle")
    var ntitle: String,
    @SerializedName("ancontent")
    var ncontent:String,
    @SerializedName("anwriter")
    var nwriter:String,
    @SerializedName("andate")
    var ndate:String
)