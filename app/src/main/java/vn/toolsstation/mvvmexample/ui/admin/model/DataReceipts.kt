package vn.toolsstation.mvvmexample.ui.admin.model

import com.google.gson.annotations.SerializedName

open class DataReceipts (
    @SerializedName("data")
    val data: List<Receipts>,
    @SerializedName("total")
    val total: Int
)