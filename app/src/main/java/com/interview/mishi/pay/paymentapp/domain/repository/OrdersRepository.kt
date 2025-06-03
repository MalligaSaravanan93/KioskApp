package com.interview.mishi.pay.paymentapp.domain.repository

import com.interview.mishi.pay.paymentapp.domain.model.OrderItem
import com.interview.mishi.pay.paymentapp.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {
    fun getOrders(): Flow<ResponseState<List<OrderItem>>>
    suspend fun createOrder(item: OrderItem): Flow<ResponseState<Unit>>
}