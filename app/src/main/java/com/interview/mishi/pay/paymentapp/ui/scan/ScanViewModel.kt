package com.interview.mishi.pay.paymentapp.ui.scan

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.paymentapp.domain.usecase.CartUseCase
import com.interview.mishi.pay.paymentapp.utils.ResponseState
import com.interview.mishi.pay.paymentapp.utils.UIState
import com.interview.mishi.pay.paymentapp.utils.toProductItem
import com.interview.mishi.pay.paymentapp.utils.value
import com.interview.mishi.pay.scanner.QRScanner
import com.interview.mishi.pay.scanner.ScanResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val cartUseCase: CartUseCase
) : ViewModel() {

    val scannedProduct: MutableState<CartItem?> = mutableStateOf(null)

    private var _addCartItem: MutableStateFlow<UIState<Unit>> =
        MutableStateFlow(UIState.Init)

    val addCartItem: StateFlow<UIState<Unit>> get() = _addCartItem

    fun scan(){
        QRScanner.scanProduct { result ->
            when (result) {
                is ScanResult.Success -> {
                    scannedProduct.value = result.product.toProductItem()
                }

                is ScanResult.Error -> {}
            }
        }
    }

    fun addCartItem(item: CartItem){
        loading()
        viewModelScope.launch {
            cartUseCase.addCartItem(item).collect{ response ->
                    when (response) {
                        is ResponseState.Success -> {
                            _addCartItem.value = UIState.Success(response.data)
                            scannedProduct.value = null
                        }

                        is ResponseState.Error -> {
                            _addCartItem.value = UIState.Error(response.e?.message ?: "")
                        }
                    }
            }
        }
    }

    fun addQuantity(){
        scannedProduct.value?.let {
            scannedProduct.value = it.copy(quantity = it.quantity.value() + 1)
        }
    }

    fun decreaseQuantity(){
        scannedProduct.value?.let {
            if(it.quantity.value() > 0 ) {
                scannedProduct.value = it.copy(quantity = it.quantity.value() - 1)
            }
        }
    }

    fun loading() {
        _addCartItem.value = UIState.Loading
    }

    fun reset() {
        _addCartItem.value = UIState.Init
    }
}