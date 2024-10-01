package vn.toolsstation.mvvmexample.http

open class BaseResponse<T>(
    val data: T,
    val code: Int,
    val status: String
)