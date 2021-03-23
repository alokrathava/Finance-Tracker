package com.example.spendit.data


import com.google.gson.annotations.SerializedName

data class Exdetail(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("amount_id")
    val amountId: String,
    @SerializedName("amount_set")
    val amountSet: String,
    @SerializedName("amt_date")
    val amtDate: String,
    @SerializedName("categories_name")
    val categoriesName: String
)