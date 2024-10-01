package vn.toolsstation.mvvmexample.ui.admin.model

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id_order")
    val idOrder: Int = 0,
    @SerializedName("name_customer")
    val nameCustomer: String = "",
    @SerializedName("address_customer")
    val addressCustomer: String = "",
    @SerializedName("email_customer")
    val emailCustomer: String = "",
    @SerializedName("created_date")
    val createdDate: String = "",
    @SerializedName("is_paid")
    val isPaid: Int = 0,
    @SerializedName("order_detail")
    val orderDetail: String = ""
)
