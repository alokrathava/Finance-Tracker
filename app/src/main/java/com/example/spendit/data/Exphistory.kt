package com.example.spendit.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class Exphistory {
    @SerializedName("amount_id")
    @Expose
    var amountId: String? = null

    @SerializedName("category_id")
    @Expose
    var categoryId: String? = null

    @SerializedName("amount")
    @Expose
    var amount: String? = null

    @SerializedName("amount_set")
    @Expose
    var amountSet: String? = null

    @SerializedName("categories_name")
    @Expose
    var categoriesName: String? = null
}