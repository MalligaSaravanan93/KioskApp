package com.interview.mishi.pay.paymentapp.ui.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.paymentapp.domain.model.OrderItem
import com.interview.mishi.pay.paymentapp.ui.components.LabelContent
import com.interview.mishi.pay.paymentapp.ui.components.NetworkImage
import com.interview.mishi.pay.paymentapp.ui.components.OrderSummaryLayout
import com.interview.mishi.pay.paymentapp.utils.displayFormat
import com.interview.mishi.pay.paymentapp.utils.toAmount
import com.interview.mishi.pay.paymentapp.utils.value

@Composable
fun OrderDetailsScreen(item: OrderItem) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        LabelContent("Invoice #: ${item.invoiceNo.value()}")
        LabelContent("Invoice Date: ${item.createdTime.displayFormat()}")
        LabelContent("Products:")
        if (item.itemsList != null) {
            LazyColumn {
                items(
                    count = item.itemsList.size,
                    key = { index -> item.itemsList[index].id.value() }
                ) { index ->
                    OrderList(item.itemsList[index])
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OrderSummaryLayout("Subtotal", item.subTotal.toAmount())
        OrderSummaryLayout("Shipping", item.shipping.toAmount())
        OrderSummaryLayout("Estimated Tax", item.tax.toAmount())
        OrderSummaryLayout("Total", item.total.toAmount())
    }
}

@Composable
fun OrderList(item: CartItem){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        NetworkImage(
            item.image.value(),
            contentDescription = item.name.value(),
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
        ) {
            LabelContent(
                item.name.value(),
                textAlign = TextAlign.Start
            )
            Row(
                modifier = Modifier.wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LabelContent("£${item.price}", modifier = Modifier.weight(1f))
                LabelContent("Qty: ${item.quantity}", modifier = Modifier.weight(1f))
                LabelContent("£${(item.quantity.value() * item.price.value()).toAmount()}")
            }
        }
    }
}