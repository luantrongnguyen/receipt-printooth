package vn.toolsstation.mvvmexample.ui.admin.model

import com.google.gson.annotations.SerializedName

data class Receipts(
    @SerializedName("id_receipt")
    val idReceipt: Int = 0,

    @SerializedName("created_at")
    val createdAt: String = "", // You can use LocalDateTime for better date handling

    @SerializedName("delivery_address")
    val deliveryAddress: String = "",

    @SerializedName("name_customer")
    val nameCustomer: String = "",

    @SerializedName("phone")
    val phone: String = "",

    @SerializedName("total")
    val total: Double = 0.0,

    @SerializedName("discount_percent")
    val discountPercent: Double = 0.0,

    @SerializedName("email")
    val email: String = ""
)
