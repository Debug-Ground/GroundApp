package com.uitlab.nogadacompany.user

import com.google.gson.annotations.SerializedName

data class UserDTO (
    @SerializedName("uid")
    var uid: Int = 0,
    @SerializedName("uemail")
    var uemail:String,
    @SerializedName("realname")
    var realname: String,
    @SerializedName("uthumbnailImageUrl")
    var uthumbnailImageUrl:String
)