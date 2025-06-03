package com.interview.mishi.pay.paymentapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class CartItem(
    val id: Int? = 0,
    val name: String? = "",
    val desc: String? = "",
    val price: Double? = 0.0,
    val image: String? = "",
    var quantity: Int? = 0,
    @ServerTimestamp
    var updatedTime: Date? = null
)
