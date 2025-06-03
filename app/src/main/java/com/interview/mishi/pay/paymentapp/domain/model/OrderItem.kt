package com.interview.mishi.pay.paymentapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class OrderItem(
    val invoiceNo: String? = "",
    val itemsList: List<CartItem>? = listOf(),
    @ServerTimestamp
    val createdTime: Date? = null,
    val subTotal: Double? = 0.0,
    val shipping: Double? = 0.0,
    val tax: Double? = 0.0,
    val total: Double? = 0.0,
    val orderStatus: Int? = 0 // 0 - Newly Generated - 1 - Ready to Deliver - 2 - Delivered
)