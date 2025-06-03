package com.interview.mishi.pay.paymentapp.utils

sealed class UIState<out T> {
    data object Init : UIState<Nothing>()
    data object Loading : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Error(val error: String) : UIState<Nothing>()
}