package vn.toolsstation.mvvmexample.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thegovape.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}