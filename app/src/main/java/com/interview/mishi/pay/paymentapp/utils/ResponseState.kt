package com.interview.mishi.pay.paymentapp.utils

sealed class ResponseState<out T> {

    data class Success<out T>(
        val data: T
    ): ResponseState<T>()

    data class Error(
        val e: AppError? = AppError.GeneralError
    ): ResponseState<Nothing>()
}

sealed class AppError(message: String): Exception(message){
    data object GeneralError: AppError(GENERAL_ERROR)
    data class CustomError(val error: String): AppError(error)
    companion object{
        const val GENERAL_ERROR = "Something went wrong. Please try again later."
    }
}