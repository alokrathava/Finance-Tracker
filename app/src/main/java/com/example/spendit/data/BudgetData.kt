package com.example.spendit.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BudgetData {
    @SerializedName("set_amount_id")
    @Expose
    var setAmountId: String? = null

    @SerializedName("categories_name")
    @Expose
    var categories_name: String? = null

    @SerializedName("amount_set")
    @Expose
    var amountSet: String? = null
}