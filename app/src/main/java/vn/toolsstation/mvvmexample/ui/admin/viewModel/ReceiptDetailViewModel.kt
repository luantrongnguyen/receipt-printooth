package vn.toolsstation.mvvmexample.ui.admin.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.toolsstation.mvvmexample.http.RetrofitInstance
import vn.toolsstation.mvvmexample.http.BaseResponse
import vn.toolsstation.mvvmexample.ui.admin.model.DataReceipts
import vn.toolsstation.mvvmexample.ui.admin.model.ReceiptDetail
import vn.toolsstation.mvvmexample.ui.admin.model.Receipts

class ReceiptDetailViewModel : ViewModel() {
    private val _ordersData : MutableStateFlow<List<ReceiptDetail>> = MutableStateFlow(listOf())
    val ordersData : StateFlow<List<ReceiptDetail>> = _ordersData

    fun retrieveReceiptData(id:Int){
        viewModelScope.launch {
            val call : Call<BaseResponse<MutableList<ReceiptDetail>>> = RetrofitInstance.apiService.getReceiptData(id)
            call.enqueue(object : Callback<BaseResponse<MutableList<ReceiptDetail>>> {
                override fun onResponse(
                    call: Call<BaseResponse<MutableList<ReceiptDetail>>>,
                    response: Response<BaseResponse<MutableList<ReceiptDetail>>>
                ) {
                    if(response.isSuccessful){
                        val responseData: MutableList<ReceiptDetail>? = response.body()?.data
                        if(responseData != null){
                            _ordersData.value = responseData
                        }
                    }
                }
                override fun onFailure(call: Call<BaseResponse<MutableList<ReceiptDetail>>>, t: Throwable) {
                    Log.d("Failed Retrieve", t.message.toString())
                }
            })
        }
    }
}