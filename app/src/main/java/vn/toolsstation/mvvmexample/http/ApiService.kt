package vn.toolsstation.mvvmexample.http

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import vn.toolsstation.mvvmexample.ui.admin.model.DataReceipts
import vn.toolsstation.mvvmexample.ui.admin.model.ReceiptDetail

interface  ApiService {
    @GET("receipts/get-all")
    fun getOrders(): Call<BaseResponse<DataReceipts>>
    @GET("receipt-detail/{id}/products")
    fun getReceiptData(@Path("id") id: Int): Call<BaseResponse<MutableList<ReceiptDetail>>>
}