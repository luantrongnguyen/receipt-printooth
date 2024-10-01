package vn.toolsstation.mvvmexample.ui.admin.model

import com.google.gson.annotations.SerializedName

data class ReceiptDetail(
    @SerializedName("id")
    val id: Int = 0, // Optional: Only if you need to reference it

    @SerializedName("quantity")
    val quantity: Int = 0,

    @SerializedName("price")
    val price: String = "", // You can switch to Double if price is always a number

    @SerializedName("name")
    val name: String = "",

    @SerializedName("id_receipt")
    val idReceipt: Int = 0 // Reference to the related ChildProduct
)
