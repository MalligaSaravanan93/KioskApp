package com.interview.mishi.pay.paymentapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.scanner.Product
import java.text.SimpleDateFormat
import java.util.Date

fun Product.toProductItem(): CartItem = CartItem(
    this.id,
    this.name,
    this.desc,
    this.price,
    this.image,
    updatedTime = Date()
)

fun String?.value() = this ?: ""
fun Int?.value() = this ?: 0
fun Double?.value() = this ?: 0.0

@SuppressLint("DefaultLocale")
fun Double?.toAmount(): String = String.format("%.2f", this.value())

fun showMessage(
    context: Context,
    resourceId: Int
) = Toast.makeText(context, context.resources.getString(resourceId), Toast.LENGTH_LONG).show()


@SuppressLint("SimpleDateFormat")
fun Date?.displayFormat() =
    if (this != null) SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(this) else ""