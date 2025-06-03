package com.interview.mishi.pay.paymentapp.domain.usecase

import com.interview.mishi.pay.paymentapp.domain.model.OrderItem
import com.interview.mishi.pay.paymentapp.domain.repository.OrdersRepository
import javax.inject.Inject

class OrdersUseCase @Inject constructor(
    private val repository: OrdersRepository
) {
    fun getOrders() = repository.getOrders()

    suspend fun createOrder(item: OrderItem) = repository.createOrder(item)
}