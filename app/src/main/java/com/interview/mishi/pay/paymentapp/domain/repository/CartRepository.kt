package com.interview.mishi.pay.paymentapp.domain.repository

import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.paymentapp.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun getCartItems(): Flow<ResponseState<List<CartItem>>>
    suspend fun addCartItem(item: CartItem): Flow<ResponseState<Unit>>
    suspend fun updateCartItem(item: CartItem): Flow<ResponseState<Unit>>
}