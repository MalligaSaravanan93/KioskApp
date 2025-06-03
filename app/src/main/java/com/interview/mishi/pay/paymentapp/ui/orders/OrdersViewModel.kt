package com.interview.mishi.pay.paymentapp.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.mishi.pay.paymentapp.domain.model.OrderItem
import com.interview.mishi.pay.paymentapp.domain.usecase.OrdersUseCase
import com.interview.mishi.pay.paymentapp.utils.ResponseState
import com.interview.mishi.pay.paymentapp.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersUseCase: OrdersUseCase
) : ViewModel() {

    private var _orderItems: MutableStateFlow<UIState<List<OrderItem>>> =
        MutableStateFlow(UIState.Init)

    val orderItems: StateFlow<UIState<List<OrderItem>>> get() = _orderItems

    fun getOrders() {
        loading()
        viewModelScope.launch {
            ordersUseCase.getOrders().collect { response ->
                when (response) {
                    is ResponseState.Success -> {
                        _orderItems.value = UIState.Success(response.data)
                    }

                    is ResponseState.Error -> {
                        _orderItems.value = UIState.Error(response.e?.message ?: "")
                    }
                }
            }
        }
    }

    fun loading() {
        _orderItems.value = UIState.Loading
    }

    fun reset() {
        _orderItems.value = UIState.Init
    }
}