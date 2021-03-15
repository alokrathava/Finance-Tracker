package com.example.spendit.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Category {
    @SerializedName("categories_name")
    @Expose
    var categoriesName: String? = null

    @SerializedName("category_id")
    @Expose
    var categoryId: String? = null
}