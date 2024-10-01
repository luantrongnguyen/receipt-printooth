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
import vn.toolsstation.mvvmexample.ui.admin.model.Receipts

class ReceiptViewModel : ViewModel() {
    private val _ordersData : MutableStateFlow<List<Receipts>> = MutableStateFlow(listOf())
    val ordersData : StateFlow<List<Receipts>> = _ordersData

    fun retrieveAgentsData(){
        viewModelScope.launch {
            val call : Call<BaseResponse<DataReceipts>> = RetrofitInstance.apiService.getOrders()
            call.enqueue(object : Callback<BaseResponse<DataReceipts>> {
                override fun onResponse(
                    call: Call<BaseResponse<DataReceipts>>,
                    response: Response<BaseResponse<DataReceipts>>
                ) {
                    if(response.isSuccessful){
                        val responseData: List<Receipts>? = response.body()?.data?.data
                        if(responseData != null){
                            _ordersData.value = responseData
                        }
                    }
                }
                override fun onFailure(call: Call<BaseResponse<DataReceipts>>, t: Throwable) {
                    Log.d("Failed Retrieve", t.message.toString())
                }
            })
        }
    }
}