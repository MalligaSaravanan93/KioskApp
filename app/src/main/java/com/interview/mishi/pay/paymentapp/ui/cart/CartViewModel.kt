package com.interview.mishi.pay.paymentapp.ui.cart

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.paymentapp.domain.model.OrderItem
import com.interview.mishi.pay.paymentapp.domain.usecase.CartUseCase
import com.interview.mishi.pay.paymentapp.domain.usecase.OrdersUseCase
import com.interview.mishi.pay.paymentapp.utils.InvoiceUtils
import com.interview.mishi.pay.paymentapp.utils.ResponseState
import com.interview.mishi.pay.paymentapp.utils.UIState
import com.interview.mishi.pay.paymentapp.utils.value
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartUseCase: CartUseCase,
    private val ordersUseCase: OrdersUseCase
) : ViewModel() {

    val subTotal = mutableDoubleStateOf(0.0)
    val shipping = mutableDoubleStateOf(0.0)
    val tax = mutableDoubleStateOf(0.0)
    val total = mutableDoubleStateOf(0.0)

    private var _cartItems: MutableStateFlow<UIState<List<CartItem>>> =
        MutableStateFlow(UIState.Init)

    val cartItems: StateFlow<UIState<List<CartItem>>> get() = _cartItems

    private var _updateCartItem: MutableStateFlow<UIState<Unit>> =
        MutableStateFlow(UIState.Init)

    val updateCartItem: StateFlow<UIState<Unit>> get() = _updateCartItem

    fun getCartItems() {
        loading()
        viewModelScope.launch {
            cartUseCase.getCartItems().collect { response ->
                when (response) {
                    is ResponseState.Success -> {
                        _cartItems.value = UIState.Success(response.data)
                        updateCartStatus(response.data)
                    }

                    is ResponseState.Error -> {
                        updateCartStatus(listOf())
                        _cartItems.value = UIState.Error(response.e?.message ?: "")
                    }
                }
            }
        }
    }

    fun updateQuantity(item: CartItem) {
        _updateCartItem.value = UIState.Loading
        viewModelScope.launch {
            cartUseCase.updateCartItem(item).collect { response ->
                when (response) {
                    is ResponseState.Success -> {
                        _updateCartItem.value = UIState.Success(response.data)
                    }

                    is ResponseState.Error -> {
                        _updateCartItem.value = UIState.Error(response.e?.message ?: "")
                    }
                }
            }
        }
    }

    private fun updateCartStatus(items: List<CartItem>) {
        _cartItems.value = UIState.Success(items)
        subTotal.doubleValue = 0.0
        items.forEach {
            subTotal.doubleValue += it.quantity.value() * it.price.value()
        }
        shipping.doubleValue = (subTotal.doubleValue / 100) * 10
        tax.doubleValue = (subTotal.doubleValue / 100) * 5
        total.doubleValue = subTotal.doubleValue + shipping.doubleValue + tax.doubleValue
    }

    fun createOrder(cartItems: List<CartItem>) {
        _updateCartItem.value = UIState.Loading
        viewModelScope.launch {
            val orderItem = OrderItem(
                invoiceNo = InvoiceUtils.generateInvoiceNumber(),
                itemsList = cartItems,
                subTotal = subTotal.doubleValue,
                shipping = shipping.doubleValue,
                tax = tax.doubleValue,
                total = total.doubleValue
            )
            ordersUseCase.createOrder(orderItem).collect { response ->
                when (response) {
                    is ResponseState.Success -> {
                        updateCartStatus(listOf())
                        _updateCartItem.value = UIState.Success(response.data)
                    }

                    is ResponseState.Error -> {
                        _updateCartItem.value = UIState.Error(response.e?.message ?: "")
                    }
                }
            }
        }
    }


    fun loading() {
        _cartItems.value = UIState.Loading
    }

    fun resetCartItems() {
        _cartItems.value = UIState.Init
    }

    fun resetUpdateItem() {
        _updateCartItem.value = UIState.Init
    }
}