package com.interview.mishi.pay.paymentapp.domain.usecase

import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.paymentapp.domain.repository.CartRepository
import javax.inject.Inject

class CartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    fun getCartItems() = repository.getCartItems()

    suspend fun addCartItem(item: CartItem) = repository.addCartItem(item)

    suspend fun updateCartItem(item: CartItem) = repository.updateCartItem(item)
}