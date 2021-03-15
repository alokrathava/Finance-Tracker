package com.example.spendit.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Users {
    @SerializedName("u_id")
    @Expose
    var uId: String? = null

    @SerializedName("u_name")
    @Expose
    var uName: String? = null

    @SerializedName("u_email")
    @Expose
    var uEmail: String? = null

    @SerializedName("u_password")
    @Expose
    var uPassword: String? = null

    @SerializedName("u_mob")
    @Expose
    var uMob: String? = null
}