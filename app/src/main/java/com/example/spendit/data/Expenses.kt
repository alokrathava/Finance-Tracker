package com.example.spendit.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Expenses {
    @SerializedName("set_amount_id")
    @Expose
    var setAmountId: String? = null

    @SerializedName("category_id")
    @Expose
    var categoryId: String? = null

    @SerializedName("amount_set")
    @Expose
    var amountSet: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
}